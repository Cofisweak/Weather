package com.cofisweak.service;

import com.cofisweak.dao.LocationRepository;
import com.cofisweak.dto.WeatherCardActionButtonType;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.exception.CannotGetApiResponseException;
import com.cofisweak.exception.LocationAlreadyExistsException;
import com.cofisweak.exception.LocationNotFoundException;
import com.cofisweak.exception.UnauthorizedException;
import com.cofisweak.model.Location;
import com.cofisweak.model.User;
import com.cofisweak.service.weather.OpenWeatherApiService;
import com.cofisweak.service.weather.WeatherService;
import com.google.gson.Gson;
import org.hibernate.SessionFactory;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationService {
    private final LocationRepository locationRepository;
    private final WeatherService weatherService;

    public LocationService(SessionFactory sessionFactory, Gson gson, HttpClient httpClient) {
        locationRepository = new LocationRepository(sessionFactory);
        weatherService = new OpenWeatherApiService(gson, httpClient);
    }

    public void saveLocation(Location location) throws LocationAlreadyExistsException {
        locationRepository.save(location);
    }

    public List<WeatherDto> getWeatherBySavedUserLocations(User user) throws CannotGetApiResponseException {
        List<Location> locations = getLocationsByUser(user);
        List<WeatherDto> result = new ArrayList<>();
        for (Location location : locations) {
            WeatherDto weatherDto = weatherService.getWeatherByLocation(location);
            weatherDto.setId(location.getId());
            weatherDto.setButtonType(WeatherCardActionButtonType.CAN_UNFOLLOW);
            result.add(weatherDto);
        }
        return result;
    }

    public List<Location> getLocationsByUser(User user) {
        return locationRepository.getByUser(user);
    }

    public void removeLocationById(User user, long locationId) throws LocationNotFoundException, UnauthorizedException {
        Optional<Location> optionalLocation = locationRepository.getById(locationId);
        Location location = optionalLocation.orElseThrow(() -> new LocationNotFoundException("Location not found"));
        if (location.getUser().getId() != user.getId()) {
            throw new UnauthorizedException("You cannot delete this location");
        }
        locationRepository.delete(location);
    }
}
