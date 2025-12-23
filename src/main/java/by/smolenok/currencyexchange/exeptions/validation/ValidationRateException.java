package by.smolenok.currencyexchange.exeptions.validation;

public class ValidationRateException extends ValidationException {
    public ValidationRateException(String message) {
        super(message);
    }

    public ValidationRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
