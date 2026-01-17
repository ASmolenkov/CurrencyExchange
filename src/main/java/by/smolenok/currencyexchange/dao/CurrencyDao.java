package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {
    Currency save(Currency currency);

    List<Currency> findAll();

    Optional<Currency>  findByCode(String code) throws ModelNotFoundException;

}
