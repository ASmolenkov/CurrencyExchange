package by.smolenok.currencyexchange.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil(){}

    public static void sendJson(Object o, int code, HttpServletResponse response) throws IOException {
        response.setStatus(code);
        objectMapper.writeValue(response.getWriter(), o);
    }

    public static void sendError(String message, int errorCode, HttpServletResponse response) throws IOException {
        sendMessage(message, errorCode, response);
    }

    public static void sendMessage(String message, int errorCode, HttpServletResponse response) throws IOException {
        Map<String, String> messages = new HashMap<>();
        messages.put("message", message);
        sendJson(messages, errorCode, response);
    }
}
