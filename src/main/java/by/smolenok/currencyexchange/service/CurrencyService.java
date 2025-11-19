package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dto.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.BusinessLogicException;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<CurrencyResponseDto> getAllCurrencies() throws DataAccessException {

        List<CurrencyResponseDto> currencyResponses = new ArrayList<>();
        List<Currency> currencies = currencyDao.findAll();
        if(!currencies.isEmpty()){
            for (Currency currency: currencies){
                CurrencyResponseDto currencyResponseDto = CurrencyMapper.toResponse(currency);
                currencyResponses.add(currencyResponseDto);
            }
        }else {
            log.error("No currencies found in database");
            throw new BusinessLogicException("No currencies found in database");
        }
        return currencyResponses;
    }
}
