package com.cofisweak.servlet;

import com.cofisweak.dto.WeatherDto;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.LocationService;
import com.cofisweak.util.SessionUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

@WebServlet(name = "indexServlet")
public class IndexServlet extends BaseServlet {
    private LocationService locationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        Gson gson = (Gson) servletContext.getAttribute("gson");
        HttpClient httpClient = (HttpClient) servletContext.getAttribute("httpClient");
        locationService = new LocationService(sessionFactory, gson, httpClient);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (SessionUtil.isUserAuthed(req)) {
            Session session = (Session) req.getAttribute("session");
            User user = session.getUser();
            List<WeatherDto> weatherList = locationService.getWeatherBySavedUserLocations(user);
            webContext.setVariable("weatherList", weatherList);
        }
        templateEngine.process("index", webContext, resp.getWriter());
    }
}
