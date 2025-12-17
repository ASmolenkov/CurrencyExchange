package by.smolenok.currencyexchange.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@UtilityClass
public class ParseUtil {
    public BigDecimal parseExchangeRate(String rate) {

        return new BigDecimal(rate);
    }

    public String parseCurrencyCode(String currencyCode) {

        return currencyCode.replaceAll("^/+", "").toUpperCase();
    }

    public String parseParameter(HttpServletRequest request) throws IOException {
        String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        String rate = null;

        if (body != null && !body.trim().isEmpty()) {
            String[] parts = body.split("=");
            if (parts.length >= 2 && "rate".equals(parts[0])) {
                rate = parts[1];
            }
        }
        return rate;
    }
}
