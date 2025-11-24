package by.smolenok.currencyexchange.mapper;

import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.model.Currency;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class CurrencyMapper {

    public CurrencyResponseDto toResponse(Currency currency) {
        return new CurrencyResponseDto(currency.getId(), currency.getName(), currency.getCode(), currency.getSign());
    }

    public Currency resultSetToCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("full_name"))
                .code(resultSet.getString("code"))
                .sign(resultSet.getString("sign"))
                .build();

    }

    public CurrencyResponseDto resultSetToResponse(ResultSet resultSet) throws SQLException {
        Currency currency = resultSetToCurrency(resultSet);
        return toResponse(currency);
    }
}
