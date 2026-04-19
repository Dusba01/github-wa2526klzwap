package utils;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;

import static utils.DBConnection.dotenv;

public class StorageService {

    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();
    private static final String BUCKET = dotenv.get("R2_BUCKET_NAME");
    private static final String UPLOAD_PREFIX = "uploads/";
    private static final String accountId = dotenv.get("R2_ACCOUNT_ID");
    private static final String accessKey = dotenv.get( "R2_ACCESS_KEY");
    private static final String secretKey = dotenv.get( "R2_SECRET_KEY");
    private final S3Client s3;
    private final String publicBaseUrl;


    public StorageService() {

        this.publicBaseUrl = getOptionalConfig("R2_PUBLIC_BASE_URL");

        this.s3 = S3Client.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .chunkedEncodingEnabled(false)
                                .build()
                )
                .build();
    }

    public String upload(InputStream inputStream, String fileName, long size) {
        String objectKey = UPLOAD_PREFIX + fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
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
                .bucket(BUCKET)
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
