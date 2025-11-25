package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dto.request.CurrencyRequestDto;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<CurrencyResponseDto> getCurrencies() throws DataAccessException {
        return currencyDao.findAll().stream().map(CurrencyMapper::toResponse).collect(Collectors.toList());
    }

    public CurrencyResponseDto getCurrency(String codeCurrency) throws ModelNotFoundException {
        log.info("Start getCurrencyByCode");
        Currency currency = currencyDao.findByCode(codeCurrency);
        return CurrencyMapper.toResponse(currency);
    }

    public CurrencyResponseDto createCurrency(CurrencyRequestDto currencyRequest) throws DataAccessException, UniqueDataException {
        if(currencyDao.existsByCode(currencyRequest.getCode())){
            throw new UniqueDataException(currencyRequest.getCode());
        }
        Currency currency = Currency.builder()
                .name(currencyRequest.getName())
                .code(currencyRequest.getCode())
                .sign(currencyRequest.getSign())
                .build();
        Currency currencyUpdate = currencyDao.save(currency);
        return CurrencyMapper.toResponse(currencyUpdate);
    }
}
