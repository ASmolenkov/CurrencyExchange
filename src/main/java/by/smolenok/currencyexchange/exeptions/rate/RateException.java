package by.smolenok.currencyexchange.exeptions.rate;

import by.smolenok.currencyexchange.exeptions.CurrencyExchangeException;

public class RateException extends CurrencyExchangeException {
    public RateException(String message) {
        super(message);
    }

    public RateException(String message, Throwable cause) {
        super(message, cause);
    }
}
