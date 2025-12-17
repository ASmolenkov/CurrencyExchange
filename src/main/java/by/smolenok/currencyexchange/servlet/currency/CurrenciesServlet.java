package by.smolenok.currencyexchange.servlet.currency;

import by.smolenok.currencyexchange.dto.request.CurrencyRequestDto;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.service.CurrencyService;
import by.smolenok.currencyexchange.utils.ApplicationConfig;
import by.smolenok.currencyexchange.utils.JsonUtil;
import by.smolenok.currencyexchange.utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_SIGN = "sign";
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException {
        this.currencyService = ApplicationConfig.getCurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyResponseDto> currencyResponses = currencyService.getCurrencies();
        if (currencyResponses.isEmpty()) {
            JsonUtil.sendMessage(ErrorType.NO_CURRENCY.getMessage(), HttpServletResponse.SC_OK, resp);
            return;
        }
        JsonUtil.sendJson(currencyResponses, HttpServletResponse.SC_OK, resp);

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
