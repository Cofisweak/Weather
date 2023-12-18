package com.cofisweak.servlet.location;

import com.cofisweak.exception.InvalidFieldException;
import com.cofisweak.exception.UnauthorizedException;
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
import java.net.http.HttpClient;

@WebServlet("/unfollow")
public class UnfollowLocationServlet extends BaseServlet {
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

        String locationId = req.getParameter("id");
        if (Utils.isFieldNotFilled(locationId)) {
            throw new InvalidFieldException("Invalid field 'id'");
        }

        try {
            long id = Long.parseLong(locationId);
            locationService.removeLocationById(user, id);
        } catch (NumberFormatException e) {
            throw new InvalidFieldException("Invalid field 'id'");
        }
        Utils.redirectToMainPage(req, resp);
    }
}
