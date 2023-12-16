package com.cofisweak.util;

import com.cofisweak.model.Session;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtil {

    public static boolean isSessionExpired(Session session) {
        return LocalDateTime.now().isAfter(session.getExpiresAt());
    }

    public static void resetCookie(Cookie cookie, HttpServletResponse response) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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
}
