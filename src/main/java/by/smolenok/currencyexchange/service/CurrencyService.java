package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dao.JdbsCurrencyDao;
import by.smolenok.currencyexchange.dto.request.CurrencyRequestDto;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import by.smolenok.currencyexchange.utils.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CurrencyService {
    private final CurrencyDao jdbsCurrencyDao;

    public CurrencyService(CurrencyDao jdbsCurrencyDao) {
        this.jdbsCurrencyDao = jdbsCurrencyDao;
    }

    public List<CurrencyResponseDto> getCurrencies() throws DataAccessException {
        return jdbsCurrencyDao.findAll().stream().map(CurrencyMapper::toResponse).collect(Collectors.toList());
    }

    public CurrencyResponseDto getCurrency(String codeCurrency) throws ModelNotFoundException {
        log.info("Start getCurrencyByCode");
        Currency currency = jdbsCurrencyDao.findByCode(codeCurrency);
        return CurrencyMapper.toResponse(currency);
    }

    public CurrencyResponseDto createCurrency(CurrencyRequestDto currencyRequest) throws DataAccessException, UniqueDataException {
        if(jdbsCurrencyDao.existsByCode(currencyRequest.code())){
            throw new UniqueDataException(currencyRequest.code());
        }
        Currency currency = Currency.builder()
                .name(currencyRequest.name())
                .code(currencyRequest.code())
                .sign(currencyRequest.sign())
                .build();
        Currency currencyUpdate = jdbsCurrencyDao.save(currency);
        return CurrencyMapper.toResponse(currencyUpdate);
    }
}
