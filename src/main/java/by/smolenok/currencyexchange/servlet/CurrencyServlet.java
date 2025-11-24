package by.smolenok.currencyexchange.servlet;

import by.smolenok.currencyexchange.dto.request.CurrencyRequestDto;
import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
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
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            String codeCurrency = PathUtils.extractCurrencyCode(path);
            log.info("Полученный путь {}", codeCurrency);
            if (ValidationUtils.codeIsEmpty(codeCurrency)) {
                List<CurrencyResponseDto> currencyResponses = currencyService.getCurrencies();
                JsonUtil.sendJson(currencyResponses, HttpServletResponse.SC_OK, resp);
                return;
            }
            CurrencyResponseDto currencyResponse = currencyService.getCurrency(codeCurrency);
            JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_OK, resp);
        } catch (DataAccessException e) {
            log.error("Service temporarily unavailable", e);
            JsonUtil.sendError("Service temporarily unavailable", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        } catch (BusinessLogicException e) {
            log.error("Currencies is not found", e);
            JsonUtil.sendError("Currencies is not found", HttpServletResponse.SC_NOT_FOUND, resp);
        } catch (ValidationCodeException e) {
            log.error("Incorrect currency code", e);
            JsonUtil.sendError("Incorrect currency code!", HttpServletResponse.SC_BAD_REQUEST, resp);
        } catch (ModelNotFoundException e) {
            log.error("Currency is not Found", e);
            JsonUtil.sendError("Currency is not Found", HttpServletResponse.SC_NOT_FOUND, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");
            CurrencyRequestDto currencyRequest;
            if(sign == null){
                currencyRequest = CurrencyRequestDto.builder().name(name).code(code).build();
            }else {
                currencyRequest = CurrencyRequestDto.builder().name(name).code(code).sign(sign).build();
            }

            CurrencyResponseDto currencyResponse = currencyService.createCurrency(currencyRequest);
            if(currencyResponse.getId() != 0){
                log.info("New currency created!");
                JsonUtil.sendJson(currencyResponse, HttpServletResponse.SC_CREATED, resp);
            }
        }catch (DataAccessException e){
            log.error("Service temporarily unavailable");
            JsonUtil.sendError("Service temporarily unavailable", HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
        }catch (UniqueDataException e){
            log.warn("Currency with this code already exists.");
            JsonUtil.sendError("Currency with this code already exists", HttpServletResponse.SC_CONFLICT, resp);
        }

    }
}
