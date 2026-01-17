package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.mapper.ExchangeRateMapper;
import by.smolenok.currencyexchange.model.ExchangeRate;
import by.smolenok.currencyexchange.utils.DatabaseManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcExchangeRatesDao implements ExchangeRatesDao {
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

    private static final String SQL_INSERT = """
            INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
            VALUES ((SELECT (currencies.id)
                     FROM currencies
                     WHERE code = ?), (SELECT (currencies.id)
                                           FROM currencies
                                           WHERE code = ?), ?)
            """;

    private static final String SQL_UPDATE = """
            UPDATE exchange_rates
            SET rate = ?
            WHERE base_currency_id = (SELECT (id) FROM currencies WHERE code = ?)
              AND target_currency_id = (SELECT (id) FROM currencies WHERE code = ?);
            """;


    @Override
    public List<ExchangeRate> findAll() {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = stmt.executeQuery()) {

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                ExchangeRate exchangeRate = ExchangeRateMapper.resultSetToExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DataAccessException(ErrorType.ERROR_RETRIEVING_EXCHANGE_RATES.getMessage());
        }
    }

    @Override
    public Optional<ExchangeRate>  findByCode(String baseCode, String targetCode) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SQL_FIND_BY_CODE)) {
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();

                }
                ExchangeRate exchangeRate = ExchangeRateMapper.resultSetToExchangeRate(resultSet);
                return Optional.of(exchangeRate);
            }
        } catch (SQLException e) {
            throw new DataAccessException(ErrorType.ERROR_RETRIEVING_EXCHANGE_RATES.getMessage());
        }
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exchangeRate.getBaseCurrency().getCode());
            stmt.setString(2, exchangeRate.getTargetCurrency().getCode());
            stmt.setBigDecimal(3, exchangeRate.getRate());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Expected 1 row to be inserted, but 0 affected.");
            }

            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    return ExchangeRate.builder()
                            .id(id)
                            .baseCurrency(exchangeRate.getBaseCurrency())
                            .targetCurrency(exchangeRate.getTargetCurrency())
                            .rate(exchangeRate.getRate())
                            .build();
                } else {
                    log.error("Creating exchange rate failed, no ID obtained.");
                    throw new DataAccessException(ErrorType.INCORRECT_EXCHANGE_RATE_NO_ID.getMessage());
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains(ErrorType.DUPLICATE_RECORD.getMessage())) {
                String baseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
                String targetCurrencyCode = exchangeRate.getTargetCurrency().getCode();
                throw new UniqueDataException(ErrorType.EXCHANGE_RATE_ALREADY_EXISTS.getMessage()
                        .formatted(baseCurrencyCode, targetCurrencyCode));
            }else {
                throw new DataAccessException(ErrorType.EXCHANGE_RATE_SAVE_FAILED.getMessage());
            }
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate updated) {
        String baseCode = updated.getBaseCurrency().getCode();
        String targetCode = updated.getTargetCurrency().getCode();
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE)) {
            stmt.setBigDecimal(1, updated.getRate());
            stmt.setString(2, baseCode);
            stmt.setString(3, targetCode);
            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                throw new ModelNotFoundException(
                        ErrorType.EXCHANGE_RATE_NOT_FOUND_TEMPLATE.getMessage()
                                .formatted(baseCode, targetCode)
                );
            }
            return updated;
        } catch (SQLException e) {
            throw new DataAccessException(ErrorType.DATABASE_ERROR.getMessage());
        }
    }
}
