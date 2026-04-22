package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    //  to connect to the database
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // to obtain the connection
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // forces driver loading
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
