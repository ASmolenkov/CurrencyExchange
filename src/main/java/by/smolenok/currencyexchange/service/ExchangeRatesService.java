package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dao.ExchangeRatesDao;
import by.smolenok.currencyexchange.dto.request.ExchangeRateRequestDto;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.mapper.ExchangeRateMapper;
import by.smolenok.currencyexchange.model.Currency;
import by.smolenok.currencyexchange.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class ExchangeRatesService {
    private final ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao();
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRatesResponseDto> getExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRatesDao.findAll();
        List<ExchangeRatesResponseDto> exchangeRatesResponses = new ArrayList<>();
        for (ExchangeRate er: exchangeRates){
            ExchangeRatesResponseDto exchangeRatesResponseDto = ExchangeRateMapper.toResponse(er);
            exchangeRatesResponses.add(exchangeRatesResponseDto);
        }
        return exchangeRatesResponses;
    }

    public ExchangeRatesResponseDto getExchangeRatesByCode(String code) throws DataAccessException, ModelNotFoundException {
        String baseCode = code.substring(0, 3);
        String targetCode = code.substring(3);
        log.info("Base Code = {}, Target Code = {}", baseCode, targetCode);
        ExchangeRate exchangeRate = exchangeRatesDao.findByCode(baseCode,targetCode);
        return ExchangeRateMapper.toResponse(exchangeRate);
    }

    //TODO Нужно придумать как извлекать Currency для отправки в Дао
    public ExchangeRatesResponseDto createExchangeRates(ExchangeRateRequestDto rateRequestDto) {
        String baseCurrencyCode = rateRequestDto.baseCurrency().code();
        String targetCurrencyCode = rateRequestDto.targetCurrency().code();
        if(exchangeRatesDao.existsByCode(baseCurrencyCode, targetCurrencyCode)){
            throw new UniqueDataException(ErrorType.EXCHANGE_RATES_EXISTS_TEMPLATE.getMessage()
                    .formatted(baseCurrencyCode, targetCurrencyCode));
        }
        if(!currencyDao.existsByCode(baseCurrencyCode)){
            throw new ModelNotFoundException(ErrorType.CURRENCY_NOT_FOUND_TEMPLATE.getMessage().formatted(baseCurrencyCode));
        }
        if(!currencyDao.existsByCode(targetCurrencyCode)){
            throw new ModelNotFoundException(ErrorType.CURRENCY_NOT_FOUND_TEMPLATE.getMessage().formatted(targetCurrencyCode));
        }
        Currency baseCurrency = Currency.builder().code(baseCurrencyCode).build();
        Currency targetCurrency = Currency.builder().code(targetCurrencyCode).build();
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .build();
        ExchangeRate updateRate = exchangeRatesDao.save(exchangeRate);
        return ExchangeRateMapper.toResponse(updateRate);

    }


}
