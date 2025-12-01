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


    private static final String TARGET = "target";
    private static final String BASE = "base";

    private static final String COLUMN_LABEL_ID = "%s_id";
    private static final String COLUMN_LABEL_NAME = "%s_name";
    private static final String COLUMN_LABEL_CODE = "%s_code";
    private static final String COLUMN_LABEL_SIGN = "%s_sign";

    private static final String COLUMN_LABEL_RATE_ID = "rate_id";
    private static final String COLUMN_LABEL_RATE = "rate";

    public ExchangeRatesResponseDto toResponse(ExchangeRate exchangeRate){
        CurrencyResponseDto baseCurrencyResponse = CurrencyMapper.toResponse(exchangeRate.getBaseCurrency());
        CurrencyResponseDto targetCurrencyResponse = CurrencyMapper.toResponse(exchangeRate.getTargetCurrency());
        return new ExchangeRatesResponseDto(exchangeRate.getId(), baseCurrencyResponse, targetCurrencyResponse, exchangeRate.getRate());
    }

    public ExchangeRate resultSetToExchangeRate(ResultSet resultSet) throws SQLException {

        Currency currencyBase = Currency.builder()
                .id(resultSet.getInt(COLUMN_LABEL_ID.formatted(BASE)))
                .name(resultSet.getString(COLUMN_LABEL_NAME.formatted(BASE)))
                .code(resultSet.getString(COLUMN_LABEL_CODE.formatted(BASE)))
                .sign(resultSet.getString(COLUMN_LABEL_SIGN.formatted(BASE)))
                .build();

        Currency currencyTarget = Currency.builder()
                .id(resultSet.getInt(COLUMN_LABEL_ID.formatted(TARGET)))
                .name(resultSet.getString(COLUMN_LABEL_NAME.formatted(TARGET)))
                .code(resultSet.getString(COLUMN_LABEL_CODE.formatted(TARGET)))
                .sign(resultSet.getString(COLUMN_LABEL_SIGN.formatted(TARGET)))
                .build();

        return ExchangeRate.builder()
                .id(resultSet.getInt(COLUMN_LABEL_RATE_ID))
                .baseCurrency(currencyBase)
                .targetCurrency(currencyTarget)
                .rate(resultSet.getBigDecimal(COLUMN_LABEL_RATE))
                .build();
    }
}
