package by.smolenok.currencyexchange.utils;


import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.validation.ValidationCodeException;
import by.smolenok.currencyexchange.exeptions.validation.ValidationException;
import by.smolenok.currencyexchange.exeptions.validation.ValidationNameException;
import by.smolenok.currencyexchange.exeptions.validation.ValidationSignException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ValidationUtils {

    public void validationName(String name){
        if(isEmpty(name)){
            throw new ValidationNameException(ErrorType.CURRENCY_NAME_MISSING.getMessage());
        }
        if(name.length() > 35){
            throw new ValidationNameException(ErrorType.INVALID_CURRENCY_NAME_LENGTH.getMessage());
        }
    }

    public void validationSign(String sign){
        if(sign.trim().length() > 1){
            throw new ValidationSignException(ErrorType.INVALID_CURRENCY_SIGN_LENGTH_TEMPLATE.getMessage().formatted(sign));
        }
    }

    public void validateCurrencyCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationCodeException(ErrorType.CURRENCY_CODE_MISSING.getMessage());
        }
        if (code.length() != 3) {
            throw new ValidationCodeException(ErrorType.INVALID_CURRENCY_CODE_LENGTH_TEMPLATE.getMessage().formatted(code));
        }
        if (!code.matches("[A-Za-z]{3}")) {
            throw new ValidationCodeException(ErrorType.INVALID_CURRENCY_CODE_TEMPLATE.getMessage().formatted(code));
        }
    }

    public void validatePairCode(String baseCode, String targetCode){
        validateCurrencyCode(baseCode);
        validateCurrencyCode(targetCode);

        if(baseCode.equalsIgnoreCase(targetCode)){
            throw new ValidationCodeException(ErrorType.SAME_CURRENCY_PAIRS_TEMPLATE.getMessage().formatted(baseCode, targetCode));
        }
    }

    public void validateExchangeRatesCode(String code) {
        if (isEmpty(code)) {
            throw new ValidationCodeException(ErrorType.EXCHANGE_RATE_PAIR_MISSING.getMessage());
        }
        if (code.length() != 6) {
            throw new ValidationCodeException(ErrorType.EXCHANGE_RATE_PAIR_LENGTH_INVALID.getMessage().formatted(code));
        }
        if (!code.matches("[A-Za-z]{6}")) {
            throw new ValidationCodeException(ErrorType.EXCHANGE_RATE_PAIR_LENGTH_INVALID.getMessage());
        }
    }

    public void validatePositiveNumber(String value, String fieldName){
        if (isEmpty(value)) {
            throw new ValidationException(
                    ErrorType.FIELD_REQUIRED_TEMPLATE.getMessage().formatted(fieldName)
            );
        }
        try {
            BigDecimal number = new BigDecimal(value);
            if (number.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException(
                        ErrorType.FIELD_MUST_BE_POSITIVE_TEMPLATE.getMessage().formatted(fieldName)
                );
            }
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    ErrorType.FIELD_MUST_BE_NUMBER_TEMPLATE.getMessage().formatted(fieldName)
            );
        }
    }

    public void validateRate(String rate) {
        validatePositiveNumber(rate, "rate");
    }

    public void validateAmount(String amount) {
        validatePositiveNumber(amount, "amount");
    }

    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void validateRequiredParameter(String value, String paramName) throws ValidationException {
        if (isEmpty(value)) {
            throw new ValidationException(ErrorType.PARAMETER_REQUIRED_TEMPLATE.getMessage().formatted(paramName));
        }
    }


}
