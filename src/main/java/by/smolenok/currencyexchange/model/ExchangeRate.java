package by.smolenok.currencyexchange.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRate {
    int id;
    Currency baseCurrency;
    Currency targetCurrency;
    @With
    BigDecimal rate;
}
