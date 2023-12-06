package com.cofisweak.util;

import com.cofisweak.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static TemplateEngine getTemplateEngine(HttpServletRequest req) {
        return (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
    }
    public static boolean isFieldNotFilled(String field) {
        return field == null || field.isBlank();
    }

    public static boolean isCorrectPassword(String username) {
        return username.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,20}$");
    }

    public static void saveSessionCookie(HttpServletResponse resp, Session session) {
        Cookie cookie = new Cookie("session", session.getId().toString());
        resp.addCookie(cookie);
    }

    public static Optional<Cookie> getCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(value -> value.getName().equals("session"))
                .findFirst();
    }

    public static boolean isUserAuthed(HttpServletRequest req) {
        return req.getAttribute("session") != null;
    }

    public static void redirectToMainPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }

    public static boolean isSessionExpired(Session session) {
        return LocalDateTime.now().isAfter(session.getExpiresAt());
    }

    public static void resetCookie(Cookie cookie, HttpServletResponse response) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
