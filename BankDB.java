package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BankDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/BankRecord";
    private static final String UserName = "postgres";
    private static final String PassWord = "123";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, UserName, PassWord);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}