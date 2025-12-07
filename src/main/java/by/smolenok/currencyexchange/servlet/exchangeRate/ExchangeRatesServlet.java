package by.smolenok.currencyexchange.servlet.exchangeRate;

import by.smolenok.currencyexchange.dto.request.ExchangeRateRequestDto;
import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.utils.ApplicationConfig;
import by.smolenok.currencyexchange.utils.JsonUtil;
import by.smolenok.currencyexchange.utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() throws ServletException {
        this.exchangeRatesService = ApplicationConfig.getExchangeRatesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRatesResponseDto> exchangeRates = exchangeRatesService.getExchangeRates();
        if (exchangeRates.isEmpty()) {
            JsonUtil.sendMessage(ErrorType.NO_EXCHANGE_RATES.getMessage(), HttpServletResponse.SC_OK, resp);
            return;
        }
        JsonUtil.sendJson(exchangeRates, HttpServletResponse.SC_OK, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        ValidationUtils.validatePairCode(baseCurrencyCode, targetCurrencyCode);
        ValidationUtils.validateRate(rate);
        BigDecimal rateNumber = ValidationUtils.parseExchangeRate(rate);

        ExchangeRateRequestDto rateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rateNumber);
        ExchangeRatesResponseDto exchangeRatesResponseDto = exchangeRatesService.createExchangeRates(rateRequestDto);
        JsonUtil.sendJson(exchangeRatesResponseDto, HttpServletResponse.SC_OK, resp);
    }
}
