package by.smolenok.currencyexchange.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtils {
    public String extractCurrencyCode(String pathInfo) {
        if(pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)){
            return null;
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
