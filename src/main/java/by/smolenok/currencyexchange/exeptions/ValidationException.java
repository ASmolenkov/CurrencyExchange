package by.smolenok.currencyexchange.exeptions;

public class ValidationException extends CurrencyExchangeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
