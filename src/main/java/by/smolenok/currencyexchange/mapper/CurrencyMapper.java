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
        return new CurrencyResponseDto(currency.getId(), currency.getFullName(), currency.getCode(), currency.getSign());
    }

    public Currency resultSetToCurrency(ResultSet resultSet) throws SQLException {
        if(resultSet.next()){
            return new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("full_name"),
                    resultSet.getString("sign")
            );
        }else {
            throw new ModelNotFoundException("Currency not found");
        }

    }

    public CurrencyResponseDto resultSetToResponse(ResultSet resultSet) throws SQLException {
        Currency currency = resultSetToCurrency(resultSet);
        return toResponse(currency);
    }
}
