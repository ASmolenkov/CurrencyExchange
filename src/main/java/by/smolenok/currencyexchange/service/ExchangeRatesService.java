package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.ExchangeRatesDao;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.mapper.ExchangeRateMapper;
import by.smolenok.currencyexchange.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class ExchangeRatesService {
    private final ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDao();

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
}
