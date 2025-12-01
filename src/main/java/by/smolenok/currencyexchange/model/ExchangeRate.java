package by.smolenok.currencyexchange.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;

@Value
@With
@Builder
public class ExchangeRate {
    int id;
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
}
