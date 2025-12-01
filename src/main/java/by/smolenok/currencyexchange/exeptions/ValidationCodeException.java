package by.smolenok.currencyexchange.exeptions;

public class ValidationCodeException extends ValidationException {

    public ValidationCodeException(String message) {
        super(message);
    }

    public ValidationCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
