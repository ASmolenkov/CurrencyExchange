package by.smolenok.currencyexchange.config;

import by.smolenok.currencyexchange.dto.response.ErrorResponse;
import by.smolenok.currencyexchange.dto.response.ErrorResponseFactory;
import by.smolenok.currencyexchange.enums.ErrorType;
import by.smolenok.currencyexchange.exeptions.DataAccessException;
import by.smolenok.currencyexchange.exeptions.ModelNotFoundException;
import by.smolenok.currencyexchange.exeptions.UniqueDataException;
import by.smolenok.currencyexchange.exeptions.validation.ValidationException;
import by.smolenok.currencyexchange.utils.JsonUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
@WebFilter("/*")
public class ExceptionHandlerFilter implements Filter {
    private final ErrorResponseFactory errorResponseFactory = new ErrorResponseFactory();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            handleException(e, httpServletResponse);
        }
    }

    private void handleException(Exception e, HttpServletResponse response) throws IOException {
        ErrorResponse error = errorResponseFactory.from(e);
        log.error(e.getMessage(), e);
        JsonUtil.sendError(error.message(), error.status(), response);
    }
}
