package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Common interface for every Data Access Object (DAO).
 *
 * <p>Each DAO encapsulates the logic needed to perform a single access
 * operation on the data source (e.g. create a note, read a user). The generic
 * type {@code T} is the class of the resource the DAO is about.</p>
 *
 * <p>The use of a common interface allows callers (typically servlets) to treat
 * every DAO uniformly: build it with a borrowed {@link java.sql.Connection} and
 * the operation parameters, invoke {@link #access()}, then read the result with
 * {@link #getOutputParam()} or {@link #getOutputListParam()}.</p>
 *
 * @param <T> the type of the resource handled by the DAO.
 */
public interface DataAccessObject<T> {

    /**
     * Performs the actual access to the data source.
     *
     * @return this DAO, to allow fluent retrieval of the output parameters.
     * @throws SQLException if any error occurs while accessing the data source.
     */
    DataAccessObject<T> access() throws SQLException;

    /**
     * Returns the single-object result of the access operation, if any.
     *
     * @return the output parameter, or {@code null} if none.
     */
    T getOutputParam();

    /**
     * Returns the list result of the access operation, if any.
     *
     * @return the output list parameter, or {@code null} if none.
     */
    List<T> getOutputListParam();
}
