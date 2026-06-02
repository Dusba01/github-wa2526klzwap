package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Base servlet that gives access to the container-managed connection pool.
 *
 * <p>The pooled {@link DataSource} is declared in {@code META-INF/context.xml}
 * and referenced from {@code WEB-INF/web.xml}. It is looked up once, via JNDI,
 * when the servlet is initialised; subclasses borrow a connection from the pool
 * through {@link #getConnection()} and hand it to a DAO, which is then
 * responsible for releasing it back to the pool.</p>
 */
public abstract class AbstractDatabaseServlet extends HttpServlet {

    /** JNDI name of the pooled DataSource (must match context.xml / web.xml). */
    private static final String JNDI_NAME = "java:comp/env/jdbc/lecturenotes";

    /** The container-managed pool of database connections. */
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            final Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(JNDI_NAME);
        } catch (NamingException e) {
            dataSource = null;
            throw new ServletException(
                    "Impossible to access the connection pool " + JNDI_NAME
                            + ". Check META-INF/context.xml and WEB-INF/web.xml.", e);
        }
    }

    @Override
    public void destroy() {
        dataSource = null;
    }

    /**
     * Returns the container-managed connection pool.
     *
     * @return the pooled {@link DataSource}.
     */
    protected final DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Borrows a connection from the connection pool.
     *
     * <p>The borrowed connection is owned by the DAO it is passed to: the DAO
     * releases it back to the pool when its {@code access()} method completes.</p>
     *
     * @return a connection borrowed from the pool.
     * @throws SQLException if a connection cannot be obtained.
     */
    protected final Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
