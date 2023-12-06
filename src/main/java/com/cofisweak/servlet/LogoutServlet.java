package com.cofisweak.servlet;

import com.cofisweak.model.Session;
import com.cofisweak.service.SessionService;
import com.cofisweak.util.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private final transient SessionService sessionService = SessionService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionService.removeSession((Session)req.getAttribute("session"));
        Optional<Cookie> cookieOptional = Utils.getCookie(req.getCookies());
        if(cookieOptional.isEmpty()) return;
        Cookie cookie = cookieOptional.get();
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }
}
