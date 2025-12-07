package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dao.ExchangeRatesDao;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.dto.response.ExchangeDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;


import java.math.BigDecimal;
import java.math.RoundingMode;
@Slf4j
public class ExchangeService {
        CurrencyDao currencyDao = new CurrencyDao();
        ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao();

    public ExchangeDto getExchange(String baseCurrency, String targetCurrency, BigDecimal amount) {
        if(exchangeRatesDao.existsByCode(baseCurrency, targetCurrency)) {
            log.info("Выполняется первое условие getExchange");
            ExchangeRate exchangeRate = exchangeRatesDao.findByCode(baseCurrency, targetCurrency);
            CurrencyResponseDto baseCurrencyResponseDto = CurrencyMapper.toResponse(exchangeRate.getBaseCurrency());
            CurrencyResponseDto targetCurrencyResponseDto = CurrencyMapper.toResponse(exchangeRate.getTargetCurrency());
            BigDecimal rate = exchangeRate.getRate();
            BigDecimal convertedAmount = calculateTargetAmount(rate, amount);
            return new ExchangeDto(baseCurrencyResponseDto, targetCurrencyResponseDto, rate ,amount, convertedAmount);
        }else if(exchangeRatesDao.existsByCode(targetCurrency, baseCurrency)){
            log.info("Выполняется второе условие getExchange");
            ExchangeRate exchangeRate = exchangeRatesDao.findByCode(targetCurrency, baseCurrency);
            CurrencyResponseDto baseCurrencyResponseDto = CurrencyMapper.toResponse(exchangeRate.getTargetCurrency());
            CurrencyResponseDto targetCurrencyResponseDto = CurrencyMapper.toResponse(exchangeRate.getBaseCurrency());
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), 6, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = calculateTargetAmount(rate, amount);
            return new ExchangeDto(baseCurrencyResponseDto, targetCurrencyResponseDto, rate ,amount, convertedAmount);
        }else if((exchangeRatesDao.existsByCode("USD", baseCurrency) &&
                (exchangeRatesDao.existsByCode("USD", targetCurrency)))){
            log.info("Выполняется третье условие getExchange");
            ExchangeRate exchangeRateBase = exchangeRatesDao.findByCode("USD", baseCurrency);
            ExchangeRate exchangeRateTarget = exchangeRatesDao.findByCode("USD", targetCurrency);
            CurrencyResponseDto baseCurrencyResponseDto = CurrencyMapper.toResponse(exchangeRateBase.getTargetCurrency());
            CurrencyResponseDto targetCurrencyResponseDto = CurrencyMapper.toResponse(exchangeRateTarget.getTargetCurrency());
            log.info("Курс USD - EUR = {}", exchangeRateBase.getRate());
            log.info("Курс USD - RUB = {}", exchangeRateTarget.getRate());
            BigDecimal rate = exchangeRateTarget.getRate().divide(exchangeRateBase.getRate(), 6, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = calculateTargetAmount(rate, amount);
            return new ExchangeDto(baseCurrencyResponseDto, targetCurrencyResponseDto, rate,amount,convertedAmount);

        }
        else {
            throw new ModelNotFoundException(ErrorType.EXCHANGE_RATE_NOT_FOUND_TEMPLATE.getMessage().formatted(baseCurrency, targetCurrency));
        }
    }

    private BigDecimal calculateTargetAmount(BigDecimal rate, BigDecimal amount){
        return rate.multiply(amount);
    }
}
