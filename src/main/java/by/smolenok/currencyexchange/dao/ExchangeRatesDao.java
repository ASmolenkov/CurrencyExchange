package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.mapper.ExchangeRateMapper;
import by.smolenok.currencyexchange.model.ExchangeRate;
import by.smolenok.currencyexchange.utils.DatabaseManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExchangeRatesDao {
    private static final String SQL_FIND_ALL = """
            SELECT
                r.id AS rate_id,
            
                cb.id AS base_id,
                cb.full_name AS base_name,
                cb.code AS base_code,
                cb.sign AS base_sign,
            
                ct.id AS target_id,
                ct.full_name AS target_name,
                ct.code AS target_code,
                ct.sign AS target_sign,
            
                r.rate
            FROM exchange_rates r
                     JOIN currencies cb ON r.base_currency_id = cb.id
                     JOIN currencies ct ON r.target_currency_id = ct.id""";

    private static final String SQL_FIND_BY_CODE = """
            SELECT
                r.id AS rate_id,
            
                cb.id AS base_id,
                cb.full_name AS base_name,
                cb.code AS base_code,
                cb.sign AS base_sign,
            
                ct.id AS target_id,
                ct.full_name AS target_name,
                ct.code AS target_code,
                ct.sign AS target_sign,
            
                r.rate
            FROM exchange_rates r
                     JOIN currencies cb ON r.base_currency_id = cb.id
                     JOIN currencies ct ON r.target_currency_id = ct.id
            WHERE cb.code = ? AND ct.code = ?;
            """;



    public List<ExchangeRate> findAll(){
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet resultSet = stmt.executeQuery()) {

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()){
                ExchangeRate exchangeRate = ExchangeRateMapper.resultSetToExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DataAccessException(ErrorType.ERROR_RETRIEVING_EXCHANGE_RATES.getMessage());
        }
    }

    public ExchangeRate findByCode(String baseCode, String targetCode){
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(SQL_FIND_BY_CODE)) {
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try(ResultSet resultSet = stmt.executeQuery()) {
                if(!resultSet.next()){
                    throw new ModelNotFoundException(ErrorType.EXCHANGE_RATES_NOT_FOUND_TEMPLATE.getMessage().formatted(baseCode, targetCode));
                }
                return ExchangeRateMapper.resultSetToExchangeRate(resultSet);
            }
        } catch (SQLException e) {
            throw new DataAccessException(ErrorType.ERROR_RETRIEVING_EXCHANGE_RATES.getMessage());
        }
    }

    public boolean existsByCode(String baseCode, String targetCode) {
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_FIND_BY_CODE)) {
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try(ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }catch (SQLException e){
            throw new DataAccessException(ErrorType.SERVICE_UNAVAILABLE.getMessage(), e);
        }

    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        return null;
    }
}
