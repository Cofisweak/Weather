package com.cofisweak.listener;

import com.cofisweak.service.SessionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j;
import org.hibernate.SessionFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
@Log4j
public class RemoveExpiredSessionsListener implements ServletContextListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("Initialize RemoveExpiredSessionsListener");

        ServletContext servletContext = sce.getServletContext();
        SessionFactory sessionFactory = (SessionFactory) servletContext.getAttribute("sessionFactory");
        SessionService sessionService = new SessionService(sessionFactory);

        scheduler.scheduleAtFixedRate(() -> {
            log.debug("Check for expired sessions");
            sessionService.removeExpiredSessions();
        }, 0, 30, TimeUnit.MINUTES);
    }
}
