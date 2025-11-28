package by.smolenok.currencyexchange.mapper;

import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.model.Currency;
import by.smolenok.currencyexchange.model.ExchangeRate;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class ExchangeRateMapper {

    public ExchangeRatesResponseDto toResponse(ExchangeRate exchangeRate){
        CurrencyResponseDto baseCurrencyResponse = CurrencyMapper.toResponse(exchangeRate.getBaseCurrency());
        CurrencyResponseDto targetCurrencyResponse = CurrencyMapper.toResponse(exchangeRate.getTargetCurrency());
        return new ExchangeRatesResponseDto(exchangeRate.getId(), baseCurrencyResponse, targetCurrencyResponse, exchangeRate.getRate());
    }

    public ExchangeRate resultSetToExchangeRate(ResultSet resultSet) throws SQLException {

        Currency currencyBase = Currency.builder()
                .id(resultSet.getInt("base_id"))
                .name(resultSet.getString("base_name"))
                .code(resultSet.getString("base_code"))
                .sign(resultSet.getString("base_sign"))
                .build();

        Currency currencyTarget = Currency.builder()
                .id(resultSet.getInt("target_id"))
                .name(resultSet.getString("target_name"))
                .code(resultSet.getString("target_code"))
                .sign(resultSet.getString("target_sign"))
                .build();

        return ExchangeRate.builder()
                .id(resultSet.getInt("rate_id"))
                .baseCurrency(currencyBase)
                .targetCurrency(currencyTarget)
                .rate(resultSet.getBigDecimal("rate"))
                .build();
    }
}
