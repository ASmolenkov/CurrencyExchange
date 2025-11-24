package by.smolenok.currencyexchange.dto.request;

import lombok.*;

@Value
@Builder
@With
public class CurrencyRequestDto {
     String name;
     String code;
     String sign;
}
