package by.smolenok.currencyexchange.exeptions.rate;

import by.smolenok.currencyexchange.exeptions.CurrencyExchangeException;

public class NegativeRateException extends RateException {
    public NegativeRateException(String message) {
        super(message);
    }

    public NegativeRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
