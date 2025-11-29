package by.smolenok.currencyexchange.dto.request;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode,
                                     BigDecimal rate) {
}
