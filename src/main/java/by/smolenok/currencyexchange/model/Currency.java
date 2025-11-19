package by.smolenok.currencyexchange.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Currency {
    private int id;
    private String name;
    private String code;
    private String sign;


    public Currency() {
    }

    public Currency(int id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

}
