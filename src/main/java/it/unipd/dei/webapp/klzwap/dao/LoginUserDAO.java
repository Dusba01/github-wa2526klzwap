package it.unipd.dei.webapp.klzwap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class LoginUserDAO implements DataAccessObject<Boolean> {

    private static final String LOGIN_USER_STATEMENT =
            "SELECT 1 FROM users WHERE (username = ? OR email = ?) AND password = ?";

    private final Connection connection;
    private final String usernameOrEmail;
    private final String password;
    private Boolean authenticated;

    public LoginUserDAO(Connection connection, String usernameOrEmail, String password) {
        this.connection = Objects.requireNonNull(connection, "connection cannot be null");
        this.usernameOrEmail = Objects.requireNonNull(usernameOrEmail, "Username Or Email cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
    }

    @Override
    public LoginUserDAO access() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_USER_STATEMENT)) {
            preparedStatement.setString(1, usernameOrEmail);
            preparedStatement.setString(2, usernameOrEmail);
            preparedStatement.setString(3, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                authenticated = resultSet.next();
            }
        }

        return this;
    }

    @Override
    public Boolean getOutputParam() {
        return authenticated;
    }
}
