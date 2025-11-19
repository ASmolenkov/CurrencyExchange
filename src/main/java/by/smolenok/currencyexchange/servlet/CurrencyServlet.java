package by.smolenok.currencyexchange.servlet;

import by.smolenok.currencyexchange.dto.CurrencyResponseDto;
import by.smolenok.currencyexchange.exeptions.BusinessLogicException;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.service.CurrencyService;
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
@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            if(path == null || path.equals("/")){
                List<CurrencyResponseDto> currencyResponses = currencyService.getAllCurrencies();
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonUtil.sendJson(currencyResponses, resp);
            }
        }catch (DataAccessException e){
            JsonUtil.sendError("Service temporarily unavailable",HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }catch (BusinessLogicException e){
            log.error(e.getMessage(), e);
            JsonUtil.sendError("Currencies is not found", HttpServletResponse.SC_NOT_FOUND, resp);
        }
    }

    private String getFullUrl(HttpServletRequest req) {
        return req.getRequestURI() +
                (req.getPathInfo() != null ? req.getPathInfo() : "") +
                (req.getQueryString() != null ? "?" + req.getQueryString() : "");
    }
}
