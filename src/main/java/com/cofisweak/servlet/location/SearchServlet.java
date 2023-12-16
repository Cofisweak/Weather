package com.cofisweak.servlet.location;

import com.cofisweak.dto.WeatherCardActionButtonType;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.model.Location;
import com.cofisweak.model.Session;
import com.cofisweak.service.LocationService;
import com.cofisweak.service.weather.OpenWeatherApiService;
import com.cofisweak.service.weather.WeatherService;
import com.cofisweak.servlet.BaseServlet;
import com.cofisweak.util.SessionUtil;
import com.cofisweak.util.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends BaseServlet {
    private final WeatherService weatherService = new OpenWeatherApiService();
    private final LocationService locationService = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getParameter("q");
        if (!Utils.isFieldNotFilled(query)) {
            webContext.setVariable("query", query);
            List<WeatherDto> weatherList = weatherService.searchLocationsAndWeatherByQuery(query);
            if (SessionUtil.isUserAuthed(req)) {
                Utils.setButtonTypesForWeatherList(weatherList, WeatherCardActionButtonType.CAN_FOLLOW);
                checkIsListContainAlreadyFollowedLocations(req, weatherList);
            } else {
                Utils.setButtonTypesForWeatherList(weatherList, WeatherCardActionButtonType.NOT_AUTHORIZED);
            }
            webContext.setVariable("weatherList", weatherList);
        }

        templateEngine.process("search", webContext, resp.getWriter());
    }

    private void checkIsListContainAlreadyFollowedLocations(HttpServletRequest req, List<WeatherDto> weatherList) {
        Session session = (Session) req.getAttribute("session");
        List<Location> followedLocations = locationService.getLocationsByUser(session.getUser());
        for (WeatherDto weatherDto : weatherList) {
            for (Location followedLocation : followedLocations) {
                if (isLocationsEquals(weatherDto, followedLocation)) {
                    weatherDto.setButtonType(WeatherCardActionButtonType.ALREADY_FOLLOWED);
                }
            }
        }
    }

    private boolean isLocationsEquals(WeatherDto weatherDto, Location followedLocation) {
        return weatherDto.getLatitude().equals(followedLocation.getLatitude()) &&
               weatherDto.getLongitude().equals(followedLocation.getLongitude());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getParameter("query");
        if (Utils.isFieldNotFilled(query)) {
            resp.sendRedirect(req.getContextPath() + "/search");
        } else {
            String queryParam = URLEncoder.encode(query, StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/search?q=" + queryParam);
        }
    }
}
