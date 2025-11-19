package by.smolenok.currencyexchange.listener;

import by.smolenok.currencyexchange.utils.DatabaseManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class DatabaseInitListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("🔧 Initializing database...");
        String dbPath = sce.getServletContext().getRealPath("/WEB-INF/database/currencyExchange.db");
        if (dbPath == null) {
            String msg = "Cannot resolve database path. Ensure app is running in a servlet container (e.g., Tomcat).";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        DatabaseManager.init(dbPath);
        DatabaseManager.executeSqlScript("schema.sql");

        log.info("Database initialized successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("🧹 Application shutdown — database connections closed automatically");
    }
}
