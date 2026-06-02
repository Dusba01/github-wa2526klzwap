package dao.user;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Checks whether a user with the given email (case-insensitive) exists.
 * Output: {@code true} if such a user exists, {@code false} otherwise.
 */
public final class UserExistsByEmailDAO extends AbstractDAO<Boolean> {

    private static final String SQL =
            "SELECT 1 FROM users WHERE LOWER(email) = LOWER(?)";

    private final String email;

    public UserExistsByEmailDAO(final Connection con, final String email) {
        super(con);
        this.email = email;
    }

    @Override
    protected void doAccess() throws SQLException {
        boolean exists = false;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                exists = rs.next();
            }
        }
        outputParam = exists;
    }
}
