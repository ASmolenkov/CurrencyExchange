package by.smolenok.currencyexchange.servlet;

import by.smolenok.currencyexchange.dto.response.ExchangeDto;
import by.smolenok.currencyexchange.service.ExchangeService;
import by.smolenok.currencyexchange.utils.JsonUtil;
import by.smolenok.currencyexchange.utils.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@WebServlet("/exchange")
public class ExchangeServlet  extends HttpServlet {
    ExchangeService exchangeService = new ExchangeService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amountParam = req.getParameter("amount");

        ValidationUtils.validateCurrencyCode(from);
        ValidationUtils.validateCurrencyCode(to);
        ValidationUtils.validateAmount(amountParam);

        BigDecimal amount = new BigDecimal(amountParam);

        ExchangeDto exchangeDto = exchangeService.getExchange(from, to, amount);

        JsonUtil.sendJson(exchangeDto, HttpServletResponse.SC_OK, resp);


    }
}
