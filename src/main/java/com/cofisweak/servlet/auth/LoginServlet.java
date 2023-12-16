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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private final transient AuthService authService = new AuthService();
    private final transient SessionService sessionService = new SessionService();
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
        Map<String, Object> errors = validateRequest(username, password);

        if (!errors.isEmpty()) {
            webContext.setVariables(errors);
            handleErrors(resp, webContext, username, templateEngine);
            return;
        }

        try {
            User user = authService.login(username.trim(), password);
            Session session = sessionService.createSession(user);
            SessionUtil.saveSessionCookie(resp, session);
            Utils.redirectToMainPage(req, resp);
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            webContext.setVariable("isAuthorizationInvalid", true);
            handleErrors(resp, webContext, username, templateEngine);
        }
    }

    private static void handleErrors(HttpServletResponse resp, WebContext webContext, String username, TemplateEngine templateEngine) throws IOException {
        webContext.setVariable("inputLogin", username);
        templateEngine.process("login", webContext, resp.getWriter());
    }

    private static Map<String, Object> validateRequest(String username, String password) {
        Map<String, Object> errors = new HashMap<>();
        if (Utils.isFieldNotFilled(username) || username.trim().length() < 3) {
            errors.put("incorrectUsername", true);
        }
        if (Utils.isFieldNotFilled(password) || Utils.isInvalidPassword(password)) {
            errors.put("incorrectPassword", true);
        }
        return errors;
    }
}
