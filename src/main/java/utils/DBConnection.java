package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Provides database connections.
 *
 * <p>Primary path: a Tomcat-managed JNDI {@link DataSource} (a connection
 * pool) declared in {@code META-INF/context.xml} and referenced from
 * {@code web.xml}. This is the recommended approach (see the Web Applications
 * best-practices document, section 5.2.3).</p>
 *
 * <p>Fallback path: when no JNDI resource is available (e.g. running outside a
 * servlet container, or in unit tests), it falls back to a plain
 * {@link DriverManager} connection using configuration taken from environment
 * variables or a local {@code .env} file. No credentials are hard-coded.</p>
 */
public class DBConnection {

    /** JNDI name of the pooled DataSource (must match context.xml / web.xml). */
    private static final String JNDI_NAME = "java:comp/env/jdbc/lecturenotes";

    /** Loaded once; kept package-visible for backward compatibility. */
    static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    /** Cached pooled DataSource (null when running without a container). */
    private static volatile DataSource dataSource;

    private DBConnection() {
        // utility class
    }

    private static DataSource lookupDataSource() {
        if (dataSource == null) {
            synchronized (DBConnection.class) {
                if (dataSource == null) {
                    try {
                        Context ctx = new InitialContext();
                        dataSource = (DataSource) ctx.lookup(JNDI_NAME);
                    } catch (NamingException e) {
                        // No container / no JNDI: fall back to DriverManager.
                        dataSource = null;
                    }
                }
            }
        }
        return dataSource;
    }

    /**
     * Returns a connection. Borrowed from the pool when running inside Tomcat,
     * otherwise opened directly via DriverManager.
     */
    public static Connection getConnection() throws SQLException {
        DataSource ds = lookupDataSource();
        if (ds != null) {
            return ds.getConnection();
        }
        return getDirectConnection();
    }

    private static Connection getDirectConnection() throws SQLException {
        String url = config("DB_URL");
        String user = config("DB_USER");
        String password = config("DB_PASSWORD");

        if (url == null || url.isBlank()) {
            throw new SQLException(
                    "No JNDI DataSource and no DB_URL configured. "
                            + "Set the jdbc/lecturenotes resource or the DB_* env vars.");
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found on classpath.", e);
        }
        return DriverManager.getConnection(url, user, password);
    }

    /** Reads config from environment variables first, then from the .env file. */
    private static String config(String key) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) {
            return env;
        }
        return dotenv.get(key);
    }
}
