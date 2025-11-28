package by.smolenok.currencyexchange.utils;

import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtils {
    public String extractCurrencyCode(String pathInfo) {
        if(pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)){
            throw new ValidationException(ErrorType.NO_CURRENCY_PAIR.getMessage());
        }
        String code = pathInfo.replaceAll("^/+","");
        if(code.length() == 3){
            ValidationUtils.validateCurrencyCode(code);
        }else {
            ValidationUtils.validateExchangeRatesCode(code);
        }
        return code.toUpperCase();
    }


}
