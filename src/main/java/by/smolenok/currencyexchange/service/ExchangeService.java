package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.ExchangeRatesDao;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.dto.response.ExchangeDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import by.smolenok.currencyexchange.model.ExchangeRate;


import java.math.BigDecimal;
import java.math.RoundingMode;


public class ExchangeService {
    ExchangeRatesDao exchangeRatesDao;

    public ExchangeService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }

    public ExchangeDto getExchange(String baseCurrency, String targetCurrency, BigDecimal amount) {
        try {
            ExchangeRate direct = exchangeRatesDao.findByCode(baseCurrency, targetCurrency);
            return buildExchangeDto(direct.getBaseCurrency(), direct.getTargetCurrency(), direct.getRate(), amount);

        } catch (ModelNotFoundException ignored) {

        }
        try {
            ExchangeRate reverse = exchangeRatesDao.findByCode(targetCurrency, baseCurrency);
            BigDecimal invertedRate = BigDecimal.ONE.divide(reverse.getRate(), 6, RoundingMode.HALF_UP);
            return buildExchangeDto(reverse.getTargetCurrency(), reverse.getBaseCurrency(), invertedRate, amount);
        } catch (ModelNotFoundException ignored) {

        }

        try {
            ExchangeRate usdToBase = exchangeRatesDao.findByCode("USD", baseCurrency);
            ExchangeRate usdToTarget = exchangeRatesDao.findByCode("USD", targetCurrency);
            BigDecimal crossRate = usdToTarget.getRate().divide(usdToBase.getRate(), 6, RoundingMode.HALF_UP);
            return buildExchangeDto(usdToBase.getTargetCurrency(), usdToTarget.getTargetCurrency(), crossRate, amount);

        } catch (ModelNotFoundException ignored) {

        }
        throw new ModelNotFoundException(ErrorType.EXCHANGE_RATE_NOT_FOUND_TEMPLATE.getMessage().formatted(baseCurrency, targetCurrency));
    }

    private ExchangeDto buildExchangeDto(Currency base, Currency target, BigDecimal rate, BigDecimal baseAmount) {
        CurrencyResponseDto baseDto = CurrencyMapper.toResponse(base);
        CurrencyResponseDto targetDto = CurrencyMapper.toResponse(target);
        BigDecimal convertedAmount = rate.multiply(baseAmount);
        return new ExchangeDto(baseDto, targetDto, rate, baseAmount, convertedAmount);
    }
}
