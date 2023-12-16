package com.cofisweak.servlet.auth;

import com.cofisweak.exception.UsernameAlreadyExistsException;
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

@WebServlet("/register")
public class RegisterServlet extends BaseServlet {
    private final transient AuthService authService = new AuthService();
    private final transient SessionService sessionService = new SessionService();
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

        Map<String, Object> errors = validateRequest(username, password, repeatPassword);

        if (!errors.isEmpty()) {
            webContext.setVariables(errors);
            handleErrors(resp, webContext, username, templateEngine);
            return;
        }

        try {
            User user = authService.register(username.trim(), password);
            Session session = sessionService.createSession(user);
            SessionUtil.saveSessionCookie(resp, session);
            Utils.redirectToMainPage(req, resp);
        } catch (UsernameAlreadyExistsException e) {
            webContext.setVariable("usernameAlreadyExists", true);
            handleErrors(resp, webContext, username, templateEngine);
        }
    }

    private static void handleErrors(HttpServletResponse resp, WebContext webContext, String username, TemplateEngine templateEngine) throws IOException {
        webContext.setVariable("inputLogin", username);
        templateEngine.process("register", webContext, resp.getWriter());
    }

    private static Map<String, Object> validateRequest(String username, String password, String repeatPassword) {
        Map<String, Object> errors = new HashMap<>();
        if (Utils.isFieldNotFilled(username)) {
            errors.put("incorrectUsername", true);
        } else if (username.trim().length() < 3) {
            errors.put("tooShortUsername", true);
        }
        if (Utils.isFieldNotFilled(password) || Utils.isInvalidPassword(password)) {
            errors.put("incorrectPassword", true);
        } else if (!password.equals(repeatPassword)) {
            errors.put("passwordsNotEquals", true);
        }
        return errors;
    }
}
