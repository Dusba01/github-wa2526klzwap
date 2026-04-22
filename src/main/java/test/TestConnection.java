package test;

import utils.DBConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Successfully connected to Neon!");
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
