package by.smolenok.currencyexchange.exeptions;

public class InvalidCurrencyCodeException extends CurrencyExchangeException{
    public InvalidCurrencyCodeException(String message) {
        super(message);
    }

    public InvalidCurrencyCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCurrencyCodeException() {
    }
}
