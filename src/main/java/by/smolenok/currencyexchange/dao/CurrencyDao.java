package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import by.smolenok.currencyexchange.utils.DatabaseManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CurrencyDao {
    private static final String SQL_FIND_ALL = """
            SELECT * FROM currencies
            """;
    private static final String SQL_FIND_BY_CODE = """
            SELECT * FROM currencies
            WHERE code = ?
            """;
    private static final String SQL_INSERT = """
            INSERT INTO currencies (code, full_name, sign)
            VALUES (?, ?, ?)
            """;

    public Currency save(Currency currency) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getName());
            stmt.setString(3, currency.getSign());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                log.error("Creating currency failed, no rows affected.");
                throw new UniqueDataException("Currency with this code already exists.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return Currency.builder()
                            .id(id)
                            .name(currency.getName())
                            .code(currency.getCode())
                            .sign(currency.getSign())
                            .build();
                } else {
                    log.error("Creating currency failed, no ID obtained.");
                    throw new DataAccessException("Creating currency failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new UniqueDataException("Currency with code '" + currency.getCode() + "' already exists", e);
            } else {
                throw new DataAccessException("Error saving currency to database", e);
            }
        }
    }

    public List<Currency> findAll() {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = stmt.executeQuery()) {
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                Currency currency = CurrencyMapper.resultSetToCurrency(resultSet);
                currencies.add(currency);
            }
            log.info("Количество валюты в списке: {}", currencies.size());
            return currencies;
        } catch (SQLException e) {
            log.error("Error retrieving currencies from the database", e);
            throw new DataAccessException("Error retrieving currencies from the database", e);
        }
    }

    public Currency findByCode(String code) throws ModelNotFoundException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CODE)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            return CurrencyMapper.resultSetToCurrency(resultSet);

        } catch (SQLException e) {
            log.error("Error retrieving currencies from the database", e);
            throw new DataAccessException("Error retrieving currencies from the database", e);
        }
    }

    public boolean existsByCode(String code) {
        String sql = """
                SELECT 1 FROM currencies WHERE code = ? LIMIT 1
                """;
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            try(ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }catch (SQLException e){
            log.error("Database error while checking existence of currency code: {}", code, e);
            throw new DataAccessException("Failed to check currency existence", e);
        }

    }
}
