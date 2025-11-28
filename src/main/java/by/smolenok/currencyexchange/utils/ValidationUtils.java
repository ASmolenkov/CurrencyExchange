package by.smolenok.currencyexchange.utils;


import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {

    public void validateCurrencyCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationException(ErrorType.CURRENCY_CODE_REQUIRED.getMessage());
        }
        if (code.length() != 3) {
            throw new ValidationException(ErrorType.INVALID_CURRENCY_SIZE.getMessage());
        }
        if (!code.matches("[A-Za-z]{3}")) {
            throw new ValidationException(ErrorType.INVALID_CURRENCY_CODE.getMessage());
        }
    }

    public void validateExchangeRatesCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationException(ErrorType.EXCHANGE_RATES_CODE_REQUIRED.getMessage());
        }
        if (code.length() != 6) {
            throw new ValidationException(ErrorType.INVALID_EXCHANGE_RATES_SIZE.getMessage());
        }
        if (!code.matches("[A-Za-z]{6}")) {
            throw new ValidationException(ErrorType.INVALID_EXCHANGE_RATES_CODE.getMessage());
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
