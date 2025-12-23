package by.smolenok.currencyexchange.listener;

import by.smolenok.currencyexchange.utils.DatabaseManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@WebListener
public class DatabaseInitListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("🔧 Initializing database...");


        String dbDir = System.getProperty("user.home") + File.separator + ".currencyexchange";
        String dbPath = dbDir + File.separator + "currencyExchange.db";


        log.info("🎯 Target database file: {}", dbPath);


        DatabaseManager.init(dbPath);


        DatabaseManager.executeSqlScript("schema.sql");

        log.info("✅ Database is ready");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("🧹 Application shutdown — SQLite connections closed automatically");
    }
}