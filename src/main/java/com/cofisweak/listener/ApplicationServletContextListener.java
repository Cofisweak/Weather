package com.cofisweak.listener;

import com.cofisweak.util.HibernateUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;

import java.net.http.HttpClient;

@WebListener
public class ApplicationServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        Gson gson = new Gson();
        HttpClient httpClient = HttpClient.newHttpClient();

        context.setAttribute("sessionFactory", sessionFactory);
        context.setAttribute("gson", gson);
        context.setAttribute("httpClient", httpClient);
    }
}
