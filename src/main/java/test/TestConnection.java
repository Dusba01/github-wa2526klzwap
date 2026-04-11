package test;

import utils.DBConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Connessione a Neon riuscita!");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }
}
