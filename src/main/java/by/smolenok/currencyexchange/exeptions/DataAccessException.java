package by.smolenok.currencyexchange.exeptions;

public class DataAccessException extends CurrencyExchangeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
