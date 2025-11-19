package by.smolenok.currencyexchange.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil(){}

    public static void sendJson(Object o, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), o);
    }

    public static void sendError(String message, int errorCode, HttpServletResponse response) throws IOException {
        response.setStatus(errorCode);
        sendJson(message, response);
    }
}
