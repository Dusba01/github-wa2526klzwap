package utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Provides database connections.
 *
 * <p>All database access must go through the Tomcat-managed JNDI
 * {@link DataSource} declared in {@code META-INF/context.xml} and referenced
 * from {@code web.xml}. If the pool is missing or misconfigured, the
 * application fails clearly instead of silently opening unmanaged direct
 * connections.</p>
 */
public class DBConnection {

    /** JNDI name of the pooled DataSource (must match context.xml / web.xml). */
    private static final String JNDI_NAME = "java:comp/env/jdbc/lecturenotes";

    /** Cached pooled DataSource. */
    private static volatile DataSource dataSource;

    private DBConnection() {
        // utility class
    }

    private static DataSource lookupDataSource() throws SQLException {
        if (dataSource == null) {
            synchronized (DBConnection.class) {
                if (dataSource == null) {
                    try {
                        Context ctx = new InitialContext();
                        dataSource = (DataSource) ctx.lookup(JNDI_NAME);
                    } catch (NamingException e) {
                        throw new SQLException(
                                "Tomcat JDBC connection pool not found at " + JNDI_NAME
                                        + ". Configure META-INF/context.xml and WEB-INF/web.xml.",
                                e);
                    } catch (ClassCastException e) {
                        throw new SQLException(
                                "JNDI resource " + JNDI_NAME + " is not a javax.sql.DataSource.",
                                e);
                    }
                }
            }
        }
        return dataSource;
    }

    /**
     * Returns a connection borrowed from the Tomcat JDBC connection pool.
     */
    public static Connection getConnection() throws SQLException {
        return lookupDataSource().getConnection();
    }
}
