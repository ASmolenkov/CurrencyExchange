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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ExchangeRatesService {
    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrencyDao jdbcCurrencyDao;

    public ExchangeRatesService(ExchangeRatesDao exchangeRatesDao, CurrencyDao jdbsCurrencyDao) {
        this.exchangeRatesDao = exchangeRatesDao;
        this.jdbcCurrencyDao = jdbsCurrencyDao;
    }

    public List<ExchangeRatesResponseDto> getExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRatesDao.findAll();
        List<ExchangeRatesResponseDto> exchangeRatesResponses = new ArrayList<>();
        for (ExchangeRate er : exchangeRates) {
            ExchangeRatesResponseDto exchangeRatesResponseDto = ExchangeRateMapper.toResponse(er);
            exchangeRatesResponses.add(exchangeRatesResponseDto);
        }
        return exchangeRatesResponses;
    }

    public ExchangeRatesResponseDto getExchangeRatesByCode(String baseCode, String targetCode) throws DataAccessException, ModelNotFoundException {
        log.info("Base Code = {}, Target Code = {}", baseCode, targetCode);
        Optional <ExchangeRate>  findExchangeRate = exchangeRatesDao.findByCode(baseCode, targetCode);
        modelNotFound(findExchangeRate.isEmpty(), baseCode, targetCode);
        return ExchangeRateMapper.toResponse(findExchangeRate.get());
    }


    public ExchangeRatesResponseDto createExchangeRates(ExchangeRateRequestDto rateRequestDto) {
        String baseCurrencyCode = rateRequestDto.baseCurrencyCode();
        String targetCurrencyCode = rateRequestDto.targetCurrencyCode();

        Optional<ExchangeRate> findExchangeRate = exchangeRatesDao.findByCode(baseCurrencyCode, targetCurrencyCode);

        if(findExchangeRate.isPresent()){
            throw new UniqueDataException(ErrorType.EXCHANGE_RATE_ALREADY_EXISTS.getMessage()
                    .formatted(baseCurrencyCode, targetCurrencyCode));
        }

        Currency baseCurrency;
        Optional<Currency> oneCurrency = jdbcCurrencyDao.findByCode(baseCurrencyCode);
        modelNotFound(oneCurrency.isEmpty(),baseCurrencyCode);
        baseCurrency = oneCurrency.get();

        Currency targetCurrency;
        Optional<Currency> secondCurrency = jdbcCurrencyDao.findByCode(targetCurrencyCode);
        modelNotFound(secondCurrency.isEmpty(),targetCurrencyCode);
        targetCurrency = secondCurrency.get();


        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rateRequestDto.rate())
                .build();
        ExchangeRate updateRate = exchangeRatesDao.save(exchangeRate);
        return ExchangeRateMapper.toResponse(updateRate);

    }


    public ExchangeRatesResponseDto updateExchangeRate(ExchangeRateRequestDto exchangeRate) {
        String baseCurrencyCode = exchangeRate.baseCurrencyCode();
        String targetCurrencyCode = exchangeRate.targetCurrencyCode();
        Optional<ExchangeRate> findExisting = exchangeRatesDao.findByCode(baseCurrencyCode, targetCurrencyCode);
        modelNotFound(findExisting.isEmpty(),baseCurrencyCode, targetCurrencyCode);
        ExchangeRate existing = findExisting.get();
        ExchangeRate updated = existing.withRate(exchangeRate.rate());
        ExchangeRate saved = exchangeRatesDao.update(updated);
        return ExchangeRateMapper.toResponse(saved);

    }

    private void modelNotFound(boolean result, String baseCurrencyCode, String targetCurrencyCode){
        if(result){
            throw new ModelNotFoundException(ErrorType.EXCHANGE_RATE_NOT_FOUND_TEMPLATE.getMessage().formatted(baseCurrencyCode, targetCurrencyCode));
        }
    }

    private void modelNotFound(boolean result, String currencyCode){
        if(result){
            throw new ModelNotFoundException(ErrorType.CURRENCY_NOT_FOUND_TEMPLATE.getMessage()
                    .formatted(currencyCode));
        }
    }
}
