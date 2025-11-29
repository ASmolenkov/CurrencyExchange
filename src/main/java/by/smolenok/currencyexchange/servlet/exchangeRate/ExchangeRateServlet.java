package by.smolenok.currencyexchange.servlet.exchangeRate;

import by.smolenok.currencyexchange.dto.request.ExchangeRateRequestDto;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.exeptions.ValidationException;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.utils.JsonUtil;
import by.smolenok.currencyexchange.utils.PathUtils;
import by.smolenok.currencyexchange.utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            String code = PathUtils.extractCurrencyCode(path);
            ValidationUtils.validateExchangeRatesCode(code);
            ExchangeRatesResponseDto exchangeRatesResponse = exchangeRatesService.getExchangeRatesByCode(code);
            JsonUtil.sendJson(exchangeRatesResponse, HttpServletResponse.SC_OK,resp);
        }catch (DataAccessException e){
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }catch (ModelNotFoundException e){
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_NOT_FOUND, resp);
        }catch (ValidationException e){
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");


            ValidationUtils.validatePairCode(baseCurrencyCode, targetCurrencyCode);
            ValidationUtils.validateRate(rate);
            BigDecimal rateNumber = ValidationUtils.parseExchangeRate(rate);

            log.info("Base Currency = {}", baseCurrencyCode);
            log.info("Target Currency = {}", targetCurrencyCode);


            ExchangeRateRequestDto rateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rateNumber);
            ExchangeRatesResponseDto exchangeRatesResponseDto = exchangeRatesService.createExchangeRates(rateRequestDto);
            JsonUtil.sendJson(exchangeRatesResponseDto, HttpServletResponse.SC_OK, resp);

        }catch (ValidationException e){
            log.error("Ошибка Валидации курса", e);
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, resp);
        }catch (UniqueDataException e){
            log.error("Такие валюты уже есть в таблице", e);
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_CONFLICT, resp);
        }catch (DataAccessException e){
            log.error(e.getMessage());
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
        }catch (ModelNotFoundException e){
            log.error(e.getMessage());
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_NOT_FOUND, resp);
        }


    }
}
