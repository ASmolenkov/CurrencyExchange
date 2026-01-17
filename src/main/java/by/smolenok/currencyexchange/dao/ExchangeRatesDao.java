package by.smolenok.currencyexchange.dao;

import by.smolenok.currencyexchange.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRatesDao {
    List<ExchangeRate> findAll();

    Optional <ExchangeRate>  findByCode(String baseCode, String targetCode);

    ExchangeRate save(ExchangeRate exchangeRate);

    ExchangeRate update(ExchangeRate updated);
}
