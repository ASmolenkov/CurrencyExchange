package by.smolenok.currencyexchange.servlet;

import by.smolenok.currencyexchange.dto.response.ExchangeRatesResponseDto;
import by.smolenok.currencyexchange.service.ExchangeRatesService;
import by.smolenok.currencyexchange.utils.JsonUtil;
import by.smolenok.currencyexchange.utils.PathUtils;
import by.smolenok.currencyexchange.utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates/*")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    //TODO Добавить все проверки, исключения, обработать исключения на всех уровнях.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String codeExchangeRates = PathUtils.extractCurrencyCode(path);

        if(ValidationUtils.isEmpty(codeExchangeRates)){
            List<ExchangeRatesResponseDto> exchangeRates = exchangeRatesService.getExchangeRates();
            JsonUtil.sendJson(exchangeRates, HttpServletResponse.SC_OK,resp);
            return;
        }
    }
}
