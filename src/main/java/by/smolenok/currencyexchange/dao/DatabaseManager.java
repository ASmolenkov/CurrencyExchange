package by.smolenok.currencyexchange.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:D:/Java/Project/CurrencyExchange/src/main/resources/currencyExchange";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC driver registered successfully");
        }catch (ClassNotFoundException e){
            System.err.println("❌ SQLite JDBC driver not found");
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }

    }

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL);
        }catch (SQLException e){
            System.err.println("❌ Database connection failed: " + e.getMessage());
            throw e;
        }
    }
}
