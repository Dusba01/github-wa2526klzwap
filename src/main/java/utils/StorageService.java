package utils;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;

public class StorageService {

    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();
    private static final String UPLOAD_PREFIX = "uploads/";

    /**
     * Single shared instance. Building an {@link S3Client} also creates an HTTP
     * connection pool, so it is expensive and meant to be created once and
     * reused for the whole application lifetime - never per request.
     */
    private static volatile StorageService instance;

    private final S3Client s3;
    private final String bucket;
    private final String publicBaseUrl;

    /**
     * Returns the shared {@code StorageService}, creating it on first use
     * (double-checked locking). Reusing one instance keeps a single S3 client
     * and connection pool alive instead of building a new one on every upload,
     * download or delete.
     */
    public static StorageService getInstance() {
        StorageService local = instance;
        if (local == null) {
            synchronized (StorageService.class) {
                local = instance;
                if (local == null) {
                    local = new StorageService();
                    instance = local;
                }
            }
        }
        return local;
    }

    /**
     * Builds an S3 client for the MinIO object store (or any S3-compatible
     * service). Configuration comes entirely from environment variables /
     * the local .env file - no credentials are hard-coded:
     *
     * <ul>
     *   <li>{@code S3_ENDPOINT} - e.g. {@code http://minio:9000} inside Docker,
     *       or {@code http://localhost:9000} for local runs.</li>
     *   <li>{@code S3_ACCESS_KEY} / {@code S3_SECRET_KEY} - MinIO credentials.</li>
     *   <li>{@code S3_BUCKET_NAME} - target bucket.</li>
     *   <li>{@code S3_REGION} - signing region (default {@code us-east-1}).</li>
     * </ul>
     */
    private StorageService() {
        this.bucket = getOptionalConfig("S3_BUCKET_NAME");

        String accessKey = getOptionalConfig("S3_ACCESS_KEY");
        String secretKey = getOptionalConfig("S3_SECRET_KEY");
        String region = firstNonBlank(getOptionalConfig("S3_REGION"), "us-east-1");
        String endpoint = getOptionalConfig("S3_ENDPOINT");

        this.publicBaseUrl = getOptionalConfig("S3_PUBLIC_BASE_URL");

        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalStateException(
                    "Object storage endpoint missing. Set S3_ENDPOINT "
                            + "(e.g. http://minio:9000).");
        }

        if (accessKey == null || secretKey == null || bucket == null) {
            throw new IllegalStateException(
                    "Object storage credentials/bucket missing. Set "
                            + "S3_ACCESS_KEY, S3_SECRET_KEY and S3_BUCKET_NAME.");
        }

        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                // Bound the overall call so a connectivity problem eventually
                // fails with a clear error instead of hanging forever. The cap
                // is generous enough to upload a full-size PDF (maxFileSize is
                // 50 MB) over a slow link without tripping a premature retry.
                .overrideConfiguration(
                        ClientOverrideConfiguration.builder()
                                .apiCallTimeout(Duration.ofMinutes(5))
                                .build()
                )
                // Disable Expect:100-continue (its handshake can stall PutObject
                // against MinIO even when GETs succeed) and bound the sockets.
                .httpClientBuilder(
                        ApacheHttpClient.builder()
                                .expectContinueEnabled(false)
                                .connectionTimeout(Duration.ofSeconds(10))
                                .socketTimeout(Duration.ofMinutes(2))
                )
                // path-style access is required for MinIO. Leave chunked
                // encoding at its default (enabled): it streams the body with
                // chunk-by-chunk signing, reading the upload stream exactly once
                // forward-only. Disabling it forces a full-payload hash that
                // re-reads the stream, which breaks on the non-resettable
                // (disk-spilled) multipart streams Tomcat hands us for files
                // larger than the in-memory threshold.
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return null;
    }

    public String upload(InputStream inputStream, String fileName, long size) {
        String objectKey = UPLOAD_PREFIX + fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType("application/pdf")
                .build();

        s3.putObject(request, RequestBody.fromInputStream(inputStream, size));

        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return publicBaseUrl.replaceAll("/+$", "") + "/" + objectKey;
        }

        return objectKey;
    }

    public StoredFile download(String filePath) {
        String objectKey = normalizeObjectKey(filePath);

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        ResponseBytes<GetObjectResponse> response = s3.getObjectAsBytes(request);
        String fileName = objectKey.substring(objectKey.lastIndexOf('/') + 1);
        String contentType = response.response().contentType();

        if (contentType == null || contentType.isBlank()) {
            contentType = "application/pdf";
        }

        return new StoredFile(fileName, contentType, response.asByteArray());
    }

    public void delete(String filePath) {
        String objectKey = normalizeObjectKey(filePath);

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

        s3.deleteObject(request);
    }

    private String normalizeObjectKey(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalStateException("Missing file path for download.");
        }

        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            int uploadsIndex = filePath.indexOf("/" + UPLOAD_PREFIX);
            if (uploadsIndex >= 0) {
                return filePath.substring(uploadsIndex + 1);
            }
        }

        return filePath;
    }

    private String getRequiredConfig(String defaultValue, String... keys) {
        for (String key : keys) {
            String value = getOptionalConfig(key);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        if (defaultValue != null && !defaultValue.isBlank()) {
            return defaultValue;
        }

        throw new IllegalStateException("Missing required configuration: " + String.join(" or ", keys));
    }

    private String getOptionalConfig(String key) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return DOTENV.get(key);
    }

    public static class StoredFile {
        private final String fileName;
        private final String contentType;
        private final byte[] bytes;

        public StoredFile(String fileName, String contentType, byte[] bytes) {
            this.fileName = fileName;
            this.contentType = contentType;
            this.bytes = bytes;
        }

        public String getFileName() {
            return fileName;
        }

        public String getContentType() {
            return contentType;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }
}
