package com.cofisweak.servlet.auth;

import com.cofisweak.exception.UnauthorizedException;
import com.cofisweak.model.Session;
import com.cofisweak.service.SessionService;
import com.cofisweak.servlet.BaseServlet;
import com.cofisweak.util.SessionUtil;
import com.cofisweak.util.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    private final transient SessionService sessionService = new SessionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!SessionUtil.isUserAuthed(req)) {
            throw new UnauthorizedException("You are not authorized");
        }

        sessionService.removeSession((Session)req.getAttribute("session"));
        Optional<Cookie> cookieOptional = SessionUtil.getCookie(req.getCookies());
        if(cookieOptional.isEmpty()) return;
        Cookie cookie = cookieOptional.get();
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        Utils.redirectToMainPage(req, resp);
    }
}
