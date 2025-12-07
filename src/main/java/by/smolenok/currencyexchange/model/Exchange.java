package by.smolenok.currencyexchange.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Exchange {
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
    BigDecimal amount;
    BigDecimal convertedAmount;

}
