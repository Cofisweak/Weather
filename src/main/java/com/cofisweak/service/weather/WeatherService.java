package com.cofisweak.service.weather;

import com.cofisweak.dto.LocationInfoDto;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.model.Location;

import java.util.List;

public interface WeatherService {
    WeatherDto getWeatherByLocation(Location location);
    List<LocationInfoDto> searchLocationsByQuery(String query);
}
