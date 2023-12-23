package com.cofisweak.servlet.auth;

import com.cofisweak.exception.UsernameAlreadyExistsException;
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

@WebServlet("/register")
public class RegisterServlet extends BaseServlet {
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
        templateEngine.process("register", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeat-password");

        if (Utils.isFieldNotFilled(username)) {
            handleError("incorrectUsername", resp, null);
        } else if (isUsernameLengthIncorrect(username)) {
            handleError("tooShortUsername", resp, username);
        } else if (isIncorrectPassword(password)) {
            handleError("incorrectPassword", resp, username);
        } else if (!password.equals(repeatPassword)) {
            handleError("passwordsNotEquals", resp, username);
        } else {
            processRegistration(req, resp, username, password);
        }
    }

    private void handleError(String errorName, HttpServletResponse resp, String username) throws IOException {
        webContext.setVariable(errorName, true);
        webContext.setVariable("inputLogin", username);
        templateEngine.process("register", webContext, resp.getWriter());
    }

    private void processRegistration(HttpServletRequest req, HttpServletResponse resp, String username, String password) throws IOException {
        try {
            User user = authService.register(username.trim(), password);
            Session session = sessionService.createSession(user);
            SessionUtil.saveSessionCookie(resp, session);
            Utils.redirectToMainPage(req, resp);
        } catch (UsernameAlreadyExistsException e) {
            handleError("usernameAlreadyExists", resp, username);
        }
    }

    private static boolean isUsernameLengthIncorrect(String username) {
        return username.trim().length() < 3;
    }

    private static boolean isIncorrectPassword(String password) {
        return Utils.isFieldNotFilled(password) || Utils.isInvalidPassword(password);
    }
}
