package by.smolenok.currencyexchange.utils;


import by.smolenok.currencyexchange.exeptions.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {

    public void validateCurrencyCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationException("Currency code is required");
        }
        if (code.length() != 3) {
            throw new ValidationException("Currency code must be exactly 3 characters");
        }
        if (!code.matches("[A-Za-z]{3}")) {
            throw new ValidationException("Currency code must contain only letters");
        }
    }

    public void validateExchangeRatesCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationException("Currency code is required");
        }
        if (code.length() != 6) {
            throw new ValidationException("Currency code must be exactly 6 characters");
        }
        if (!code.matches("[A-Za-z]{6}")) {
            throw new ValidationException("Currency code must contain only letters");
        }
    }

    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void validateRequiredParameter(String value, String paramName) throws ValidationException {
        if(isEmpty(value)){
            throw new ValidationException("Parameter '" + paramName + "' is required");
        }
    }

}
