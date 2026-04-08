package test;

import java.sql.Connection;
import utils.DBConnection;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Connesso al database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
