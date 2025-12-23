package by.smolenok.currencyexchange.config;

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
        int status;
        String message;
        if (e instanceof ValidationException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = e.getMessage();
        } else if (e instanceof ModelNotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            message = e.getMessage();
        }else if(e instanceof DataAccessException){
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = e.getMessage();
        }else if(e instanceof UniqueDataException){
            status = HttpServletResponse.SC_CONFLICT;
            message = e.getMessage();
        }else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = ErrorType.DATABASE_ERROR.getMessage();
        }
        log.error(e.getMessage(), e);
        JsonUtil.sendError(message, status, response);
    }
}
