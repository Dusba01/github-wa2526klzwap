package dao.user;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Checks whether a user with the given username (case-insensitive) exists.
 * Output: {@code true} if such a user exists, {@code false} otherwise.
 */
public final class UserExistsByUsernameDAO extends AbstractDAO<Boolean> {

    private static final String SQL =
            "SELECT 1 FROM users WHERE LOWER(username) = LOWER(?)";

    private final String username;

    public UserExistsByUsernameDAO(final Connection con, final String username) {
        super(con);
        this.username = username;
    }

    @Override
    protected void doAccess() throws SQLException {
        boolean exists = false;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                exists = rs.next();
            }
        }
        outputParam = exists;
    }
}
