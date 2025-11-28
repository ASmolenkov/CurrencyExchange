package by.smolenok.currencyexchange.servlet.currency;

import by.smolenok.currencyexchange.dto.response.CurrencyResponseDto;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.service.CurrencyService;
import by.smolenok.currencyexchange.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<CurrencyResponseDto> currencyResponses = currencyService.getCurrencies();
            if(currencyResponses.isEmpty()){
                JsonUtil.sendMessage(ErrorType.NO_CURRENCY.getMessage(), HttpServletResponse.SC_OK, resp);
                return;
            }
            JsonUtil.sendJson(currencyResponses, HttpServletResponse.SC_OK, resp);
        }catch (DataAccessException e){
            JsonUtil.sendError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }

    }
}
