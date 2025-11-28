package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.mapper.ExchangeRateMapper;
import by.smolenok.currencyexchange.model.ExchangeRate;
import by.smolenok.currencyexchange.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
