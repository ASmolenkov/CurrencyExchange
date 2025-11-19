package by.smolenok.currencyexchange.mapper;

import by.smolenok.currencyexchange.dto.CurrencyResponseDto;
import by.smolenok.currencyexchange.model.Currency;

public class CurrencyMapper {

    private CurrencyMapper() {
    }

    public static CurrencyResponseDto toResponse(Currency currency) {
        return new CurrencyResponseDto(currency.getId(), currency.getName(), currency.getCode(), currency.getSign());
    }
}
