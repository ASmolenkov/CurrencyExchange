package by.smolenok.currencyexchange.servlet.exchangeRate;

import by.smolenok.currencyexchange.dto.request.ExchangeRateRequestDto;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.utils.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Slf4j
@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    public void init() throws ServletException {
        this.exchangeRatesService = ApplicationConfig.getExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String codePair = PathUtils.extractCurrencyCode(path);
        ValidationUtils.validateExchangeRatesCode(codePair);
        String baseCode = codePair.substring(0, 3);
        String targetCode = codePair.substring(3);
        ExchangeRatesResponseDto exchangeRatesResponse = exchangeRatesService.getExchangeRatesByCode(baseCode, targetCode);
        JsonUtil.sendJson(exchangeRatesResponse, HttpServletResponse.SC_OK, resp);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String rate = ParseUtil.parseParameter(req);
        String codePair = PathUtils.extractCurrencyCode(path);

        ValidationUtils.validateExchangeRatesCode(codePair);
        ValidationUtils.validateRate(rate);

        BigDecimal rateNumber = ParseUtil.parseExchangeRate(rate);
        String baseCode = codePair.substring(0, 3);
        String targetCode = codePair.substring(3);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCode, targetCode, rateNumber);
        ExchangeRatesResponseDto exchangeRatesResponseDto = exchangeRatesService.updateExchangeRate(exchangeRateRequestDto);

        JsonUtil.sendJson(exchangeRatesResponseDto, HttpServletResponse.SC_OK, resp);
    }
}
