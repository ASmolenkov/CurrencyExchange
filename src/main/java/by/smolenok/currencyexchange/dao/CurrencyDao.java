package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
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

    public Currency save(Currency currency){
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());
            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                log.error("Creating currency failed, no rows affected.");
                throw new DataAccessException("Creating currency failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    return new Currency(id, currency.getFullName(), currency.getCode(), currency.getSign());
                }else {
                    log.error("Creating currency failed, no ID obtained.");
                    throw new DataAccessException("Creating currency failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            if(e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")){
                throw new DataAccessException("Currency with code '" + currency.getCode() + "' already exists", e);
            }else {
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
                Currency currency = new Currency(resultSet.getInt("id"), resultSet.getString("code"),
                        resultSet.getString("full_name"), resultSet.getString("sign"));
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            log.error("Error retrieving currencies from the database", e);
            throw new DataAccessException("Error retrieving currencies from the database", e);
        }
    }

    public Currency findByCode(String code) throws ModelNotFoundException {
        log.info("Start findByCode");
        try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CODE)){
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            return CurrencyMapper.resultSetToCurrency(resultSet);

        }catch (SQLException e) {
            log.error("Error retrieving currencies from the database", e);
            throw new DataAccessException("Error retrieving currencies from the database", e);
        }
    }
}
