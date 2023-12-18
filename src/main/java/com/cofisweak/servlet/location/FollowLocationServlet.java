package com.cofisweak.servlet.location;

import com.cofisweak.exception.InvalidFieldException;
import com.cofisweak.exception.UnauthorizedException;
import com.cofisweak.model.Location;
import com.cofisweak.model.Session;
import com.cofisweak.model.User;
import com.cofisweak.service.LocationService;
import com.cofisweak.servlet.BaseServlet;
import com.cofisweak.util.SessionUtil;
import com.cofisweak.util.Utils;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;

@WebServlet("/follow")
public class FollowLocationServlet extends BaseServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!SessionUtil.isUserAuthed(req)) {
            throw new UnauthorizedException("You are not authorized");
        }
        Session session = (Session) req.getAttribute("session");
        User user = session.getUser();

        String name = req.getParameter("name");
        String state = req.getParameter("state");
        String longitude = req.getParameter("lon");
        String latitude = req.getParameter("lat");
        Location location = getLocation(name, longitude, latitude, state, user);

        locationService.saveLocation(location);
        Utils.redirectToMainPage(req, resp);
    }

    private static Location getLocation(String name, String longitude, String latitude, String state, User user) {
        validateRequestFields(name, longitude, latitude);
        if (Utils.isFieldNotFilled(state)) {
            state = null;
        }

        return Location.builder()
                .user(user)
                .name(name)
                .state(state)
                .longitude(new BigDecimal(longitude))
                .latitude(new BigDecimal(latitude))
                .build();
    }

    private static void validateRequestFields(String name, String longitude, String latitude) {
        if (Utils.isFieldNotFilled(name)) {
            throw new InvalidFieldException("Invalid field 'name'");
        }
        if (Utils.isFieldNotFilled(longitude)) {
            throw new InvalidFieldException("Invalid field 'lon'");
        }
        if (Utils.isFieldNotFilled(latitude)) {
            throw new InvalidFieldException("Invalid field 'lat'");
        }
    }
}
