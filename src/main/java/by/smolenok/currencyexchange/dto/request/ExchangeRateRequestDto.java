package by.smolenok.currencyexchange.dto.request;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(CurrencyRequestDto baseCurrency, CurrencyRequestDto targetCurrency,
                                     BigDecimal rate) {
}
