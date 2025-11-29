package by.smolenok.currencyexchange.utils;

import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.ValidationException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class PathUtils {
    public String extractCurrencyCode(String pathInfo) {
        if(pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)){
            throw new ValidationException(ErrorType.NO_CURRENCY_PAIR.getMessage());
        }
        return ValidationUtils.parseCurrencyCode(pathInfo);
    }
}
