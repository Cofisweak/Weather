package com.cofisweak.service.weather;

import com.cofisweak.dto.LocationInfoDto;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.dto.openweather.WeatherResponseDto;
import com.cofisweak.exception.CannotGetApiResponseException;
import com.cofisweak.mapper.LocationMapper;
import com.cofisweak.mapper.WeatherMapper;
import com.cofisweak.model.Location;
import com.cofisweak.util.PropertiesUtil;
import com.cofisweak.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherApiService implements WeatherService {
    private static final String host = PropertiesUtil.get("OPENWEATHER_HOST");
    private static final String appid = PropertiesUtil.get("OPENWEATHER_APPID");
    private final Gson gson = new Gson();

    private WeatherResponseDto searchWeatherByCoordinates(BigDecimal longitude, BigDecimal latitude) {
        try {
            URI uri = buildGetWeatherUri(longitude, latitude);
            HttpResponse<String> response = Utils.getResponse(uri);
            String content = response.body();
            return gson.fromJson(content, WeatherResponseDto.class);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new CannotGetApiResponseException("The request cannot be completed");
        }
    }

    @Override
    public WeatherDto getWeatherByLocation(Location location) {
        WeatherResponseDto weather = searchWeatherByCoordinates(location.getLongitude(), location.getLatitude());
        return WeatherMapper.mapFrom(weather, location);
    }

    @Override
    public List<WeatherDto> searchLocationsAndWeatherByQuery(String query) {
        try {
            URI uri = buildSearchLocationsUri(query);
            HttpResponse<String> response = Utils.getResponse(uri);
            String content = response.body();
            return getWeatherDtos(content);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new CannotGetApiResponseException("The request cannot be completed");
        }
    }

    private List<WeatherDto> getWeatherDtos(String content) {
        Type listType = new TypeToken<List<LocationInfoDto>>() {}.getType();
        List<LocationInfoDto> dtoList = gson.fromJson(content, listType);

        List<Location> locations = dtoList.stream().map(LocationMapper::mapFrom).toList();

        List<WeatherDto> result = new ArrayList<>();
        for (Location location : locations) {
            WeatherResponseDto dto = searchWeatherByCoordinates(location.getLongitude(), location.getLatitude());
            WeatherDto weatherDto = WeatherMapper.mapFrom(dto, location);
            result.add(weatherDto);
        }
        return result;
    }

    private URI buildGetWeatherUri(BigDecimal longitude, BigDecimal latitude) throws URISyntaxException {
        String uri = host + "data/2.5/weather?" +
                     "lat=" + latitude.toString() +
                     "&lon=" + longitude.toString() +
                     "&units=metric" +
                     "&appid=" + appid;
        return new URI(uri);
    }

    private URI buildSearchLocationsUri(String query) throws URISyntaxException {
        String queryParam = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String uri = host + "geo/1.0/direct?" +
                     "limit=9" +
                     "&q=" + queryParam +
                     "&appid=" + appid;
        return new URI(uri);
    }
}
