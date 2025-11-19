package by.smolenok.currencyexchange.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyResponseDto {
    private int id;
    private String name;
    private String code;
    private String sign;


    public CurrencyResponseDto() {
    }

    public CurrencyResponseDto(int id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

}
