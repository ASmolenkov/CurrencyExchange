package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.ExchangeRatesDao;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.mapper.ExchangeRateMapper;
import by.smolenok.currencyexchange.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

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
}
