package by.smolenok.currencyexchange.mapper;

import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.model.Currency;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class CurrencyMapper {

    private static final String COLUMN_LABEL_ID = "id";
    private static final String COLUMN_LABEL_FULL_NAME = "full_name";
    private static final String COLUMN_LABEL_CODE = "code";
    private static final String COLUMN_LABEL_SIGN = "sign";

    public CurrencyResponseDto toResponse(Currency currency) {
        return new CurrencyResponseDto(currency.getId(), currency.getName(), currency.getCode(), currency.getSign());
    }

    public Currency resultSetToCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getInt(COLUMN_LABEL_ID))
                .name(resultSet.getString(COLUMN_LABEL_FULL_NAME))
                .code(resultSet.getString(COLUMN_LABEL_CODE))
                .sign(resultSet.getString(COLUMN_LABEL_SIGN))
                .build();

    }

    public CurrencyResponseDto resultSetToResponse(ResultSet resultSet) throws SQLException {
        Currency currency = resultSetToCurrency(resultSet);
        return toResponse(currency);
    }
}
