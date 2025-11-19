package by.smolenok.currencyexchange.exeptions;

public class BusinessLogicException extends CurrencyExchangeException {
    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessLogicException() {
        super();
    }
}
