package com.cofisweak.filter;

import com.cofisweak.model.Session;
import com.cofisweak.service.SessionService;
import com.cofisweak.util.Utils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebFilter(urlPatterns = {"/*"})
public class SessionFilter implements Filter {
    private final SessionService sessionService = SessionService.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> cookieOptional = Utils.getCookie(cookies);
            if (cookieOptional.isPresent()) {
                Cookie cookie = cookieOptional.get();
                processFilter(cookie, response, request);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void processFilter(Cookie cookie, HttpServletResponse response, HttpServletRequest request) {
        try {
            String sessionId = cookie.getValue();
            UUID uuid = UUID.fromString(sessionId);

            Optional<Session> sessionOptional = sessionService.getSession(uuid);
            if (sessionOptional.isEmpty()) {
                Utils.resetCookie(cookie, response);
                return;
            }

            Session session = sessionOptional.get();
            if (Utils.isSessionExpired(session)) {
                sessionService.removeSession(session);
                Utils.resetCookie(cookie, response);
                return;
            }

            request.setAttribute("session", session);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
