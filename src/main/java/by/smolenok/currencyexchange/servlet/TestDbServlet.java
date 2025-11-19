package by.smolenok.currencyexchange.servlet;

import by.smolenok.currencyexchange.utils.DatabaseManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/test-db")
public class TestDbServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM currencies")) {

            rs.next();
            int count = rs.getInt(1);

            out.println("<h2>✅ Database is working!</h2>");
            out.println("<p>Number of currencies: <strong>" + count + "</strong></p>");
            out.println("<p>DB Path: <code>" + System.getProperty("user.dir") + "</code></p>");
            out.println("<p>Connection URL: <code>" + conn.getMetaData().getURL() + "</code></p>");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("<h2>❌ Database error</h2><pre>" + e.getMessage() + "</pre>");
        }
    }
}
