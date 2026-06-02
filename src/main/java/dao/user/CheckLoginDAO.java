package dao.user;

import dao.AbstractDAO;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Verifies the given credential (email or username) and password.
 * Output: the authenticated {@link User}, or {@code null} if invalid.
 */
public final class CheckLoginDAO extends AbstractDAO<User> {

    private static final String SQL =
            "SELECT * FROM users WHERE (email = ? OR username = ?) AND password = ?";

    private final String credential;
    private final String password;

    public CheckLoginDAO(final Connection con, final String credential, final String password) {
        super(con);
        this.credential = credential;
        this.password = password;
    }

    @Override
    protected void doAccess() throws SQLException {
        User user = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, credential);
            ps.setString(2, credential);
            ps.setString(3, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                }
            }
        }
        outputParam = user;
    }
}
