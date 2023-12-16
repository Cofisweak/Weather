package com.cofisweak.servlet.error;

import com.cofisweak.servlet.BaseServlet;
import com.cofisweak.util.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error")
public class ErrorServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode == null) {
            Utils.redirectToMainPage(req, resp);
            return;
        }
        String message = (String) req.getAttribute("jakarta.servlet.error.message");
        if (message != null) {
            if(statusCode == 404 && message.isBlank()) {
                webContext.setVariable("message", "Page not found");
            } else if(!message.isBlank()) {
                webContext.setVariable("message", message);
            }
        }
        webContext.setVariable("statusCode", statusCode);
        templateEngine.process("error-page", webContext, resp.getWriter());
    }
}
