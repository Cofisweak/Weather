package com.cofisweak.servlet;

import com.cofisweak.exception.*;
import com.cofisweak.model.Session;
import com.cofisweak.service.SessionService;
import com.cofisweak.util.SessionUtil;
import com.cofisweak.util.ThymeleafUtil;
import com.cofisweak.util.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static jakarta.servlet.http.HttpServletResponse.*;

@Log4j
public class BaseServlet extends HttpServlet {
    private final SessionService sessionService = new SessionService();
    protected TemplateEngine templateEngine;
    protected WebContext webContext;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        templateEngine = Utils.getTemplateEngine(req);
        webContext = ThymeleafUtil.buildWebContext(req, resp);
        Cookie[] cookies = req.getCookies();
        checkCookies(req, resp, cookies);

        try {
            log.debug(req.getMethod() + " " + req.getServletPath());
            super.service(req, resp);
        } catch (InvalidFieldException e) {
            log.debug(e.getMessage(), e);
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedException e) {
            log.debug(e.getMessage(), e);
            resp.sendError(SC_FORBIDDEN, e.getMessage());
        } catch (LocationNotFoundException e) {
            log.debug(e.getMessage(), e);
            resp.sendError(SC_NOT_FOUND, e.getMessage());
        } catch (LocationAlreadyExistsException e) {
            log.debug(e.getMessage(), e);
            resp.sendError(SC_CONFLICT, e.getMessage());
        } catch (CannotGetApiResponseException e) {
            log.error(e.getMessage(), e);
            resp.sendError(SC_SERVICE_UNAVAILABLE, e.getMessage());
        } catch (Exception e) {
            log.error("Unhandled servlet error", e);
            throw e;
        }
    }

    private void checkCookies(HttpServletRequest req, HttpServletResponse resp, Cookie[] cookies) {
        if (cookies != null) {
            Optional<Cookie> cookieOptional = SessionUtil.getCookie(cookies);
            if (cookieOptional.isPresent()) {
                Cookie cookie = cookieOptional.get();
                processCheckSession(cookie, resp, req);
            }
        }
    }

    private void processCheckSession(Cookie cookie, HttpServletResponse resp, HttpServletRequest req) {
        try {
            String sessionId = cookie.getValue();
            UUID uuid = UUID.fromString(sessionId);

            Optional<Session> sessionOptional = sessionService.getSession(uuid);
            if (sessionOptional.isEmpty()) {
                SessionUtil.resetCookie(cookie, resp);
                return;
            }

            Session session = sessionOptional.get();
            if (SessionUtil.isSessionExpired(session)) {
                sessionService.removeSession(session);
                SessionUtil.resetCookie(cookie, resp);
                return;
            }

            req.setAttribute("session", session);
            webContext.setVariable("isAuth", true);
            webContext.setVariable("login", session.getUser().getLogin());
        } catch (IllegalArgumentException ignored) {
        }
    }
}
