package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.model.ExchangeRate;

import java.util.List;

public interface ExchangeRatesDao {
    List<ExchangeRate> findAll();

    ExchangeRate findByCode(String baseCode, String targetCode);

    boolean existsByCode(String baseCode, String targetCode);

    ExchangeRate save(ExchangeRate exchangeRate);

    ExchangeRate update(ExchangeRate updated);
}
