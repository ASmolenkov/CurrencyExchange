package by.smolenok.currencyexchange.exeptions.rate;

public class ValidationRateException extends RateException {
    public ValidationRateException(String message) {
        super(message);
    }

    public ValidationRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
