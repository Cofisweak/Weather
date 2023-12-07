package com.cofisweak.servlet;

import com.cofisweak.exception.UsernameAlreadyExistsException;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.AuthService;
import com.cofisweak.service.SessionService;
import com.cofisweak.util.ThymeleafUtil;
import com.cofisweak.util.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.cofisweak.util.Utils.getTemplateEngine;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final transient AuthService authService = AuthService.getInstance();
    private final transient SessionService sessionService = SessionService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (Utils.isUserAuthed(req)) {
            Utils.redirectToMainPage(req, resp);
            return;
        }
        TemplateEngine templateEngine = getTemplateEngine(req);
        WebContext webContext = ThymeleafUtil.buildWebContext(req, resp);
        templateEngine.process("register", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeat-password");

        TemplateEngine templateEngine = getTemplateEngine(req);
        WebContext webContext = ThymeleafUtil.buildWebContext(req, resp);
        Map<String, Object> errors = validateRequest(username, password, repeatPassword);

        if (!errors.isEmpty()) {
            webContext.setVariables(errors);
            handleErrors(resp, webContext, username, templateEngine);
            return;
        }

        try {
            User user = authService.register(username.trim(), password);
            Session session = sessionService.createSession(user);
            Utils.saveSessionCookie(resp, session);
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
