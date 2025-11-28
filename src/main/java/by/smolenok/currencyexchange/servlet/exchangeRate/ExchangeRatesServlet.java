package by.smolenok.currencyexchange.servlet.exchangeRate;

import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
@Slf4j
@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRatesResponseDto> exchangeRates = exchangeRatesService.getExchangeRates();
            if(exchangeRates.isEmpty()){
                JsonUtil.sendMessage(ErrorType.NO_EXCHANGE_RATES.getMessage(),HttpServletResponse.SC_OK, resp);
            }
            JsonUtil.sendJson(exchangeRates, HttpServletResponse.SC_OK, resp);
        }catch (DataAccessException e){
            log.error(ErrorType.SERVICE_UNAVAILABLE.getMessage(), e);
            JsonUtil.sendError(ErrorType.SERVICE_UNAVAILABLE.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }
    }
}
