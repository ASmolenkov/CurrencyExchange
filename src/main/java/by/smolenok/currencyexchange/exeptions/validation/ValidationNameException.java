package by.smolenok.currencyexchange.exeptions.validation;

public class ValidationNameException extends ValidationException {
    public ValidationNameException(String message) {
        super(message);
    }

    public ValidationNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
