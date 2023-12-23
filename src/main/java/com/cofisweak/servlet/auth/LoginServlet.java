package com.cofisweak.servlet.auth;

import com.cofisweak.exception.IncorrectPasswordException;
import com.cofisweak.exception.UserNotFoundException;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.AuthService;
import com.cofisweak.service.SessionService;
import com.cofisweak.servlet.BaseServlet;
import com.cofisweak.util.SessionUtil;
import com.cofisweak.util.Utils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private transient AuthService authService;
    private transient SessionService sessionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        authService = new AuthService(sessionFactory);
        sessionService = new SessionService(sessionFactory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (SessionUtil.isUserAuthed(req)) {
            Utils.redirectToMainPage(req, resp);
            return;
        }
        templateEngine.process("login", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (isIncorrectUsername(username)) {
            handleError("incorrectUsername", resp, null);
        } else if (isIncorrectPassword(password)) {
            handleError("incorrectPassword", resp, username);
        } else {
            handleLogin(req, resp, username, password);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp, String username, String password) throws IOException {
        try {
            User user = authService.login(username.trim(), password);
            Session session = sessionService.createSession(user);
            SessionUtil.saveSessionCookie(resp, session);
            Utils.redirectToMainPage(req, resp);
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            webContext.setVariable("isAuthorizationFailed", true);
            handleError("isAuthorizationFailed", resp, username);
        }
    }

    private void handleError(String errorName, HttpServletResponse resp, String username) throws IOException {
        webContext.setVariable(errorName, true);
        webContext.setVariable("inputLogin", username);
        templateEngine.process("login", webContext, resp.getWriter());
    }

    private static boolean isIncorrectPassword(String password) {
        return Utils.isFieldNotFilled(password) || Utils.isInvalidPassword(password);
    }

    private static boolean isIncorrectUsername(String username) {
        return Utils.isFieldNotFilled(username) || username.trim().length() < 3;
    }
}
