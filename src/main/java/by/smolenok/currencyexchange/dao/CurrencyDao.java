package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.model.Currency;

import java.util.List;

public interface CurrencyDao {
    Currency save(Currency currency);

    List<Currency> findAll();

    Currency findByCode(String code) throws ModelNotFoundException;

    boolean existsByCode(String code);
}
