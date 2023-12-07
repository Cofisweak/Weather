package com.cofisweak.servlet;

import com.cofisweak.util.ThymeleafUtil;
import com.cofisweak.util.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = Utils.getTemplateEngine(req);
        WebContext webContext = ThymeleafUtil.buildWebContext(req, resp);
        templateEngine.process("index", webContext, resp.getWriter());
    }
}
