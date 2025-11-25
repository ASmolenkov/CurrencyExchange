package by.smolenok.currencyexchange.dto.request;

import lombok.*;

public record CurrencyRequestDto(String name, String code, String sign) {

    public CurrencyRequestDto{
        name = (name != null) ? name.trim() : null;
        code = (code != null) ? code.trim().toUpperCase() : null;
        sign = (sign != null) ? sign.trim() : null;
    }

    public static CurrencyRequestDto of(String name, String code, String sign) {
        return new CurrencyRequestDto(name, code, sign);
    }
}
