package by.smolenok.currencyexchange.dto.response;

import java.math.BigDecimal;

public record ExchangeRatesResponseDto(int id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency,
                                       BigDecimal rate) {

}
