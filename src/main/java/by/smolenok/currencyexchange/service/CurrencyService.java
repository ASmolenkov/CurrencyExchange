package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.ValidationCodeException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<CurrencyResponseDto> getCurrencies() throws DataAccessException {
        return currencyDao.findAll().stream().
                map(CurrencyMapper::toResponse).
                collect(Collectors.toList());
    }

    public CurrencyResponseDto getCurrency(String codeCurrency) throws ValidationCodeException, ModelNotFoundException {
        log.info("Start getCurrencyByCode");
        log.info("Code currency {}", codeCurrency);
        Currency currency = currencyDao.findByCode(codeCurrency);
        if(currency == null){
            log.error("No currency found in database");
            throw new ModelNotFoundException("No currency found in database");
        }
        return CurrencyMapper.toResponse(currency);
    }


}
