package by.smolenok.currencyexchange.exeptions;

public class CurrencyExchangeException extends RuntimeException{
    public CurrencyExchangeException(String message){
        super(message);
    }
    public CurrencyExchangeException(String message, Throwable cause){
        super(message, cause);
    }

    public CurrencyExchangeException() {

    }
}
