package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    //  to connect with database
    private static final String URL = dotenv.get("DB_URL", "jdbc:postgresql://ep-round-morning-alvsvcxk-pooler.c-3.eu-central-1.aws.neon.tech/neondb?sslmode=require");
    private static final String USER = dotenv.get("DB_USER", "neondb_owner");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD", "npg_TvtePx7YL6Xn");

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
