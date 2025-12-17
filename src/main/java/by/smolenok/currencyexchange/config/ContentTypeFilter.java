package by.smolenok.currencyexchange.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ContentTypeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String path = request.getRequestURI();

        // ✅ Обрабатываем корневой путь и статику — не трогаем Content-Type
        if (path.equals("/") || path.endsWith(".html") || path.endsWith(".css") || path.endsWith(".js")
                || path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // ✅ Для API — устанавливаем JSON по умолчанию
        if (response.getContentType() == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
