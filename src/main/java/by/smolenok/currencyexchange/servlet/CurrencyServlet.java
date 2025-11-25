package by.smolenok.currencyexchange.servlet;

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
import java.util.List;

@Slf4j
@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_SIGN = "sign";
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            String codeCurrency = PathUtils.extractCurrencyCode(path);
            if (ValidationUtils.isEmpty(codeCurrency)) {
                List<CurrencyResponseDto> currencyResponses = currencyService.getCurrencies();
                JsonUtil.sendJson(currencyResponses, HttpServletResponse.SC_OK, resp);
                return;
            }
            CurrencyResponseDto currencyResponse = currencyService.getCurrency(codeCurrency);
            JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_OK, resp);
        } catch (DataAccessException e) {
            log.error(ErrorType.SERVICE_UNAVAILABLE.getMessage(), e);
            JsonUtil.sendError(ErrorType.SERVICE_UNAVAILABLE.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        } catch (ValidationException e) {
            log.error(ErrorType.INVALID_CURRENCY_CODE.getMessage(), e);
            JsonUtil.sendError(ErrorType.INVALID_CURRENCY_CODE.getMessage(), HttpServletResponse.SC_BAD_REQUEST, resp);
        } catch (ModelNotFoundException e) {
            log.error(ErrorType.CURRENCY_NOT_FOUND.getMessage(), e);
            JsonUtil.sendError(ErrorType.CURRENCY_NOT_FOUND.getMessage(), HttpServletResponse.SC_NOT_FOUND, resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter(PARAM_NAME);
            String code = req.getParameter(PARAM_CODE);
            String sign = req.getParameter(PARAM_SIGN);

            ValidationUtils.validateRequiredParameter(name, PARAM_NAME);
            ValidationUtils.validateCurrencyCode(code);

            CurrencyRequestDto currencyRequest = CurrencyRequestDto.of(name, code, sign);

            CurrencyResponseDto currencyResponse = currencyService.createCurrency(currencyRequest);
            JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_CREATED, resp);

        } catch (DataAccessException e) {
            log.error(ErrorType.SERVICE_UNAVAILABLE.getMessage(), e);
            JsonUtil.sendError(ErrorType.SERVICE_UNAVAILABLE.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        } catch (UniqueDataException e) {
            log.error("Currency with this code {} already exists", e.getMessage());
            JsonUtil.sendError(ErrorType.CURRENCY_CODE_EXISTS_TEMPLATE.getMessage().formatted(e.getMessage()),
                    HttpServletResponse.SC_CONFLICT, resp);
        } catch (ValidationException e) {
            log.warn(ErrorType.VALIDATION_FAILED.getMessage(), e);
            JsonUtil.sendError(ErrorType.VALIDATION_FAILED.getMessage(), HttpServletResponse.SC_BAD_REQUEST, resp);
        }
    }

}
