package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //  to connect with database
    private static final String URL = "jdbc:postgresql://localhost:5432/lecture_notes";
    private static final String USER = "dusi";
    private static final String PASSWORD = "password";

    // to obtain the connection
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // forza il caricamento del driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}