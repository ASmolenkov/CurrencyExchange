package by.smolenok.currencyexchange.exeptions.validation;

public class ValidationSignException extends ValidationException {
    public ValidationSignException(String message) {
        super(message);
    }

    public ValidationSignException(String message, Throwable cause) {
        super(message, cause);
    }
}
