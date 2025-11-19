package by.smolenok.currencyexchange.exeptions;

public class ModelNotFoundException extends CurrencyExchangeException{
    public ModelNotFoundException(String message) {
        super(message);
    }

    public ModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
