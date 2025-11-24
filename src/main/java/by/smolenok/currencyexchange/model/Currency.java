package by.smolenok.currencyexchange.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Currency {
    private int id;
    private String fullName;
    private String code;
    private String sign;


    public Currency() {
    }

    public Currency(int id, String fullName, String code, String sign) {
        this.id = id;
        this.fullName = fullName;
        this.code = code;
        this.sign = sign;
    }

}
