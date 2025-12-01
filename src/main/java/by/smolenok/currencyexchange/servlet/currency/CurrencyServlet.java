package by.smolenok.currencyexchange.servlet.currency;

import by.smolenok.currencyexchange.dto.request.CurrencyRequestDto;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.*;
import by.smolenok.currencyexchange.service.CurrencyService;
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

    private static final String PARAM_NAME = "name";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_SIGN = "sign";
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();
        String codeCurrency = PathUtils.extractCurrencyCode(path);
        ValidationUtils.validateCurrencyCode(codeCurrency);
        CurrencyResponseDto currencyResponse = currencyService.getCurrency(codeCurrency);
        JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_OK, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(PARAM_NAME);
        String code = req.getParameter(PARAM_CODE);
        String sign = req.getParameter(PARAM_SIGN);

        ValidationUtils.validateRequiredParameter(name, PARAM_NAME);
        ValidationUtils.validateCurrencyCode(code);

        CurrencyRequestDto currencyRequest = CurrencyRequestDto.of(name, code, sign);

        CurrencyResponseDto currencyResponse = currencyService.createCurrency(currencyRequest);
        JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_CREATED, resp);
    }
}
