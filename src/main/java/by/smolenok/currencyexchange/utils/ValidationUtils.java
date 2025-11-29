package by.smolenok.currencyexchange.utils;


import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.ValidationException;
import by.smolenok.currencyexchange.exeptions.ValidationRateException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ValidationUtils {

    public void validateCurrencyCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationException(ErrorType.CURRENCY_CODE_REQUIRED.getMessage());
        }
        if (code.length() != 3) {
            throw new ValidationException(ErrorType.INVALID_CURRENCY_SIZE_TEMPLATE.getMessage().formatted(code));
        }
        if (!code.matches("[A-Za-z]{3}")) {
            throw new ValidationException(ErrorType.INVALID_CURRENCY_CODE_TEMPLATE.getMessage().formatted(code));
        }
    }

    public void validatePairCode(String baseCode, String targetCode){
        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);

        if(baseCode.equalsIgnoreCase(targetCode)){
            throw new ValidationException("Коды валют не должны быть одинаковыми! (Ваш код: '%s' - '%s')".formatted(baseCode, targetCode));
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

    public void validateRate(String rate) {
        try {
            BigDecimal result = new BigDecimal(rate);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationRateException("Exchange rate cannot be less than 0");
            }
        } catch (NumberFormatException e) {
            throw new ValidationRateException("Exchange rate должен быть числом");
        }
    }

    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void validateRequiredParameter(String value, String paramName) throws ValidationException {
        if (isEmpty(value)) {
            throw new ValidationException("Parameter '" + paramName + "' is required");
        }
    }

    public BigDecimal parseExchangeRate(String rate) {
        return new BigDecimal(rate);
    }

    public String parseCurrencyCode(String currencyCode) {
        return currencyCode.replaceAll("^/+", "").toUpperCase();
    }
}
