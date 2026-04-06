package it.unipd.dei.webapp.klzwap.dao;

import it.unipd.dei.webapp.klzwap.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public final class CreateUserDAO implements DataAccessObject<Boolean> {

    private static final String INSERT_USER_STATEMENT =
            "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

    private final Connection connection;
    private final User user;
    private Boolean created;

    public CreateUserDAO(Connection connection, User user) {
        this.connection = Objects.requireNonNull(connection, "connection cannot be null");
        this.user = Objects.requireNonNull(user, "user cannot be null");
    }

    @Override
    public CreateUserDAO access() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_STATEMENT)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());

            created = preparedStatement.executeUpdate() == 1;
        }

        return this;
    }

    @Override
    public Boolean getOutputParam() {
        return created;
    }
}
