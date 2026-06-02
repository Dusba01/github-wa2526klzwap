package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Base implementation of the {@link DataAccessObject} interface.
 *
 * <p>It gives all subclasses a uniform behaviour so that each of them only has
 * to implement the specific logic for performing the requested data access
 * operation inside {@link #doAccess()}.</p>
 *
 * <p>The {@link #access()} method takes care of:</p>
 * <ul>
 *   <li>always closing the {@link Connection} once the operation completes
 *       (success or failure);</li>
 *   <li>rolling back the transaction if an error occurs and the connection is
 *       not in auto-commit mode;</li>
 *   <li>preventing the accidental re-use of a one-shot DAO via the
 *       {@code accessed} flag guarded by a {@code lock} object.</li>
 * </ul>
 *
 * <p>DAO objects are one-shot and are not expected to be re-used. The
 * connections themselves are borrowed from the container-managed connection
 * pool: a DAO never looks up the pool, it simply uses (and releases) the
 * connection it is given.</p>
 *
 * @param <T> the type of the resource handled by the DAO.
 */
public abstract class AbstractDAO<T> implements DataAccessObject<T> {

    /** Logger shared by all the DAO subclasses. */
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /** The connection to the database, borrowed from the connection pool. */
    protected final Connection con;

    /** The single-object result of the access operation, if any. */
    protected T outputParam = null;

    /** The list result of the access operation, if any. */
    protected List<T> outputListParam = null;

    /** Guards against the (mis-)use of an already executed DAO. */
    private boolean accessed = false;

    /** Lock object protecting the {@code accessed} flag. */
    private final Object lock = new Object();

    /**
     * Builds the DAO around the given database connection.
     *
     * @param con the connection borrowed from the connection pool; never null.
     */
    protected AbstractDAO(final Connection con) {
        if (con == null) {
            throw new NullPointerException("The connection cannot be null.");
        }
        this.con = con;
    }

    @Override
    public final T getOutputParam() {
        return outputParam;
    }

    @Override
    public final List<T> getOutputListParam() {
        return outputListParam;
    }

    @Override
    public final DataAccessObject<T> access() throws SQLException {
        synchronized (lock) {
            if (accessed) {
                LOGGER.warn("DAO {} already accessed: the operation is ignored.",
                        this.getClass().getName());
                return this;
            }
            accessed = true;
        }

        try {
            doAccess();
        } catch (SQLException e) {
            LOGGER.error("Unable to perform the data access operation.", e);
            rollbackQuietly();
            throw e;
        } finally {
            closeQuietly();
        }

        return this;
    }

    /**
     * Performs the actual data access operation. Implemented by each subclass.
     *
     * @throws SQLException if any error occurs while accessing the data source.
     */
    protected abstract void doAccess() throws SQLException;

    private void rollbackQuietly() {
        try {
            if (con != null && !con.isClosed() && !con.getAutoCommit()) {
                con.rollback();
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to roll back the transaction.", e);
        }
    }

    private void closeQuietly() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to release the connection back to the pool.", e);
        }
    }
}
