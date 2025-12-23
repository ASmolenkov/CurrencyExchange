package by.smolenok.currencyexchange.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);
    private static volatile String dbUrl;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            log.info("SQLite JDBC driver registered successfully");
        }catch (ClassNotFoundException e){
            log.error("SQLite JDBC driver not found", e);
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }

    }

    private DatabaseManager() {}

    public static void  init(String dbFilePath){
        if (dbFilePath == null || dbFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Database file path cannot be null or empty");
        }


        Path path = Paths.get(dbFilePath).toAbsolutePath().normalize();
        File dbFile = path.toFile();


        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new RuntimeException("Failed to create database directory: " + parentDir);
            }
        }

        synchronized (DatabaseManager.class) {
            if (dbUrl == null) {
                dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                log.info("Initialized database at: {}", dbFile.getAbsolutePath());
            }
        }
    }
    public static Connection getConnection() throws SQLException {
        if(dbUrl == null){
            throw new IllegalArgumentException("DatabaseManager not initialized. Call init() first.");
        }
        return DriverManager.getConnection(dbUrl);
    }

    public static void executeSqlScript(String scriptResourceName) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            InputStream is = DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(scriptResourceName);
            if (is == null) {
                String msg = "SQL script not found: " + scriptResourceName;
                log.error(msg);
                throw new RuntimeException(msg);
            }

            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String[] statements = sql.split(";");

            for (String stmtStr : statements) {
                stmtStr = stmtStr.trim();
                if (!stmtStr.isEmpty()) {
                    stmt.execute(stmtStr + ";");
                }
            }
            log.info("Database schema initialized from {}", scriptResourceName);
        } catch (Exception e) {
            log.error("Failed to execute SQL script: {}", scriptResourceName, e);
            throw new RuntimeException("Failed to execute SQL script: " + scriptResourceName, e);
        }
    }
}
