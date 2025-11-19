package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.model.Currency;
import by.smolenok.currencyexchange.utils.DatabaseManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyDao {
    private static final String SQL_FIND_ALL = """
            SELECT * FROM currencies
            """;

    public List<Currency> findAll() {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = stmt.executeQuery()) {
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                Currency currency = new Currency(resultSet.getInt("id"), resultSet.getString("code"),
                        resultSet.getString("full_name"), resultSet.getString("sign"));
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new DataAccessException("Error retrieving currencies from the database", e);
        }
    }
}
