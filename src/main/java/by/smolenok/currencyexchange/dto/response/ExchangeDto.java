package by.smolenok.currencyexchange.dto.response;

import java.math.BigDecimal;

public record ExchangeDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate,
                          BigDecimal amount, BigDecimal convertedAmount) {
}
