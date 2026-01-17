package by.smolenok.currencyexchange.service;

import by.smolenok.currencyexchange.dao.CurrencyDao;
import by.smolenok.currencyexchange.dto.request.CurrencyRequestDto;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.mapper.CurrencyMapper;
import by.smolenok.currencyexchange.model.Currency;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CurrencyService {
    private final CurrencyDao jdbcCurrencyDao;

    public CurrencyService(CurrencyDao jdbsCurrencyDao) {
        this.jdbcCurrencyDao = jdbsCurrencyDao;
    }

    public List<CurrencyResponseDto> getCurrencies() throws DataAccessException {
        return jdbcCurrencyDao.findAll().stream().map(CurrencyMapper::toResponse).collect(Collectors.toList());
    }

    public CurrencyResponseDto getCurrency(String codeCurrency) throws ModelNotFoundException {
        log.info("Start getCurrencyByCode");
        Optional <Currency> currency = jdbcCurrencyDao.findByCode(codeCurrency);
        if(currency.isEmpty()){
            throw new ModelNotFoundException(ErrorType.CURRENCY_NOT_FOUND_TEMPLATE.getMessage().formatted(codeCurrency));
        }
        return CurrencyMapper.toResponse(currency.get());
    }

    public CurrencyResponseDto createCurrency(CurrencyRequestDto currencyRequest) throws DataAccessException, UniqueDataException {
        Optional<Currency> foundCurrency = jdbcCurrencyDao.findByCode(currencyRequest.code());
        if(foundCurrency.isPresent()){
            throw new UniqueDataException(ErrorType.CURRENCY_CODE_DUPLICATE.getMessage());
        }
        Currency currency = Currency.builder()
                .name(currencyRequest.name())
                .code(currencyRequest.code())
                .sign(currencyRequest.sign())
                .build();
        Currency currencyUpdate = jdbcCurrencyDao.save(currency);
        return CurrencyMapper.toResponse(currencyUpdate);
    }
}
