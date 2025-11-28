package by.smolenok.currencyexchange.servlet.exchangeRate;

import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.ValidationException;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.utils.JsonUtil;
import by.smolenok.currencyexchange.utils.PathUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            String code = PathUtils.extractCurrencyCode(path);
            log.info("Path = {}", code);
            ExchangeRatesResponseDto exchangeRatesResponse = exchangeRatesService.getExchangeRatesByCode(code);
            JsonUtil.sendJson(exchangeRatesResponse, HttpServletResponse.SC_OK,resp);
        }catch (DataAccessException e){
            JsonUtil.sendError(ErrorType.SERVICE_UNAVAILABLE.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }catch (ModelNotFoundException e){
            JsonUtil.sendError(ErrorType.EXCHANGE_RATES_NOT_FOUND.getMessage(), HttpServletResponse.SC_NOT_FOUND, resp);
        }catch (ValidationException e){
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, resp);
        }


    }
}
