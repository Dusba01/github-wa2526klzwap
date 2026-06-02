package dao.user;

import dao.AbstractDAO;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Inserts a new user. Output: the stored {@link User}.
 */
public final class InsertUserDAO extends AbstractDAO<User> {

    private static final String SQL =
            "INSERT INTO users (name, username, email, password) VALUES (?, ?, ?, ?)";

    private final User user;

    public InsertUserDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.executeUpdate();
        }
        outputParam = user;
    }
}
