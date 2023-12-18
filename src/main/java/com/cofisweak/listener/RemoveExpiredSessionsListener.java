package com.cofisweak.listener;

import com.cofisweak.service.SessionService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
@Log4j
public class RemoveExpiredSessionsListener implements ServletContextListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final SessionService sessionService = new SessionService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("Initialize RemoveExpiredSessionsListener");
        scheduler.scheduleAtFixedRate(() -> {
            log.debug("Check for expired sessions");
            sessionService.removeExpiredSessions();
        }, 0, 30, TimeUnit.MINUTES);
    }
}
