package com.cofisweak.util;

import com.cofisweak.model.Session;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThymeleafUtil {
    public static TemplateEngine buildTemplateEngine(ServletContext servletContext) {
        TemplateEngine engine = new TemplateEngine();
        IWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        ITemplateResolver templateResolver = buildTemplateResolver(application);
        engine.addTemplateResolver(templateResolver);
        return engine;
    }

    public static WebContext buildWebContext(HttpServletRequest req, HttpServletResponse resp) {
        ServletContext servletContext = req.getServletContext();
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        IServletWebExchange webExchange = application.buildExchange(req, resp);
        WebContext webContext = new WebContext(webExchange);
        Session session = (Session) req.getAttribute("session");
        if (session != null) {
            webContext.setVariable("login", session.getUser().getLogin());
        }
        return webContext;
    }

    private static ITemplateResolver buildTemplateResolver(IWebApplication webApplication) {
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(webApplication);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("/templates/");
        return templateResolver;
    }
}
