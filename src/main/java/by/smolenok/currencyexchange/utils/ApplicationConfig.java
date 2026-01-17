package by.smolenok.currencyexchange.utils;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dao.ExchangeRatesDao;
import by.smolenok.currencyexchange.dao.JdbcCurrencyDao;
import by.smolenok.currencyexchange.dao.JdbcExchangeRatesDao;
import by.smolenok.currencyexchange.service.CurrencyService;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.service.ExchangeService;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConfig {
    @Getter
    private static final CurrencyDao currencyDao = new JdbcCurrencyDao();
    @Getter
    private static final ExchangeRatesDao exchangeRatesDao = new JdbcExchangeRatesDao();

    @Getter
    private static final ExchangeService exchangeService = new ExchangeService(exchangeRatesDao);
    @Getter
    private static final ExchangeRatesService exchangeRatesService = new ExchangeRatesService(exchangeRatesDao,currencyDao);
    @Getter
    private static final CurrencyService currencyService = new CurrencyService(currencyDao);


}
