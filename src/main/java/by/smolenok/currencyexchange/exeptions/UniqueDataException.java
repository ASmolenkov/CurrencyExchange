package by.smolenok.currencyexchange.exeptions;

public class UniqueDataException extends CurrencyExchangeException{
    public UniqueDataException(String message) {
        super(message);
    }

    public UniqueDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueDataException() {
    }
}
