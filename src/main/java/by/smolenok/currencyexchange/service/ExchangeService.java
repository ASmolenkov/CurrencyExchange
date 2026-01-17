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
import java.util.Optional;


public class ExchangeService {
    private static final String USD_CODE = "USD";
    private final ExchangeRatesDao exchangeRatesDao;

    public ExchangeService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }

    public ExchangeDto getExchange(String baseCurrency, String targetCurrency, BigDecimal amount) {
        return findDirect(baseCurrency, targetCurrency, amount)
                .or(() -> findReverse(baseCurrency, targetCurrency, amount))
                .or(()-> findCross(baseCurrency, targetCurrency, amount))
                .orElseThrow(() -> new ModelNotFoundException(ErrorType.EXCHANGE_RATE_NOT_FOUND_TEMPLATE.getMessage()
                        .formatted(baseCurrency, targetCurrency)));
    }

    private ExchangeDto buildExchangeDto(Currency base, Currency target, BigDecimal rate, BigDecimal baseAmount) {
        CurrencyResponseDto baseDto = CurrencyMapper.toResponse(base);
        CurrencyResponseDto targetDto = CurrencyMapper.toResponse(target);
        BigDecimal convertedAmount = rate.multiply(baseAmount);
        return new ExchangeDto(baseDto, targetDto, rate, baseAmount, convertedAmount);
    }

    private Optional<ExchangeDto> findDirect(String base, String target, BigDecimal amount){
        return exchangeRatesDao.findByCode(base, target).map(exchangeRate -> buildExchangeDto(exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),exchangeRate.getRate(),amount));
    }

    private Optional<ExchangeDto> findReverse (String base, String target, BigDecimal amount){
        return exchangeRatesDao.findByCode(base, target).map(exchangeRate -> {
            BigDecimal invertedRate = BigDecimal.ONE.divide(exchangeRate.getRate(), 6, RoundingMode.HALF_UP);
            return buildExchangeDto(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), invertedRate, amount);
        });
    }

    private Optional<ExchangeDto> findCross(String base, String target, BigDecimal amount){
        Optional<ExchangeRate> usdToBase = exchangeRatesDao.findByCode(USD_CODE,base);
        Optional<ExchangeRate> usdToTarget = exchangeRatesDao.findByCode(USD_CODE,target);
        if(usdToBase.isPresent() && usdToTarget.isPresent()){
            BigDecimal crossRate = usdToTarget.get().getRate().divide(usdToBase.get().getRate(), 6, RoundingMode.HALF_UP);
            return Optional.of(buildExchangeDto(usdToBase.get().getTargetCurrency(),
                    usdToTarget.get().getTargetCurrency(),
                    crossRate,
                    amount));
        }
        return Optional.empty();
    }
}
