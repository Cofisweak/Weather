package com.cofisweak.listener;

import com.cofisweak.util.ThymeleafUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j;
import org.thymeleaf.TemplateEngine;

@WebListener()
@Log4j
public class ThymeleafContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("Initialize RemoveExpiredSessionsListener");
        ServletContext servletContext = sce.getServletContext();
        TemplateEngine templateEngine = ThymeleafUtil.buildTemplateEngine(servletContext);
        servletContext.setAttribute("templateEngine", templateEngine);
    }
}
