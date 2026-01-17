package by.smolenok.currencyexchange.servlet.currency;

import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.*;
import by.smolenok.currencyexchange.service.CurrencyService;
import by.smolenok.currencyexchange.utils.ApplicationConfig;
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

@Slf4j
@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        this.currencyService = ApplicationConfig.getCurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String codeCurrency = PathUtils.extractCurrencyCode(path);
        ValidationUtils.validateCurrencyCode(codeCurrency);
        CurrencyResponseDto currencyResponse = currencyService.getCurrency(codeCurrency);
        JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_OK, resp);

    }

}
