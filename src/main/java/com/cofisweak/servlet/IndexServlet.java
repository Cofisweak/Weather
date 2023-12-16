package com.cofisweak.servlet;

import com.cofisweak.dto.WeatherDto;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.LocationService;
import com.cofisweak.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "indexServlet")
public class IndexServlet extends BaseServlet {
    private final LocationService locationService = new LocationService();
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
