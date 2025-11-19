package by.smolenok.currencyexchange.utils;


import by.smolenok.currencyexchange.exeptions.ValidationCodeException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {

    public void validateCurrencyCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new ValidationCodeException("Currency code is required");
        }
        if (code.length() != 3) {
            throw new ValidationCodeException("Currency code must be exactly 3 characters");
        }
        if (!code.matches("[A-Za-z]{3}")) {
            throw new ValidationCodeException("Currency code must contain only letters");
        }
    }

    public boolean codeIsEmpty(String codeCurrency) {
        return codeCurrency == null || codeCurrency.trim().isEmpty();
    }

}
