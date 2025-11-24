package by.smolenok.currencyexchange.utils;

import by.smolenok.currencyexchange.dto.response.ErrorApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil(){}

    public static void sendJson(Object o, int code, HttpServletResponse response) throws IOException {
        response.setStatus(code);
        objectMapper.writeValue(response.getWriter(), o);
    }

    public static void sendError(String message, int errorCode, HttpServletResponse response) throws IOException {
        ErrorApi errorApi = new ErrorApi(message);
        sendJson(errorApi, errorCode, response);
    }
}
