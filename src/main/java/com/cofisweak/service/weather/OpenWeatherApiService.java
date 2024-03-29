package com.cofisweak.service.weather;

import com.cofisweak.dto.LocationInfoDto;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.dto.openweather.WeatherResponseDto;
import com.cofisweak.exception.CannotGetApiResponseException;
import com.cofisweak.mapper.WeatherMapper;
import com.cofisweak.model.Location;
import com.cofisweak.util.PropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Log4j
public class OpenWeatherApiService implements WeatherService {
    private static final String host = PropertiesUtil.get("OPENWEATHER_HOST");
    private static final String appid = PropertiesUtil.get("OPENWEATHER_APPID");
    private final Gson gson;
    private final HttpClient httpClient;

    private WeatherResponseDto searchWeatherByCoordinates(BigDecimal longitude, BigDecimal latitude) {
        try {
            String response = requestWeather(longitude, latitude);
            return gson.fromJson(response, WeatherResponseDto.class);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new CannotGetApiResponseException("The request cannot be completed", e);
        }
    }

    @Override
    public WeatherDto getWeatherByLocation(Location location) {
        WeatherResponseDto weather = searchWeatherByCoordinates(location.getLongitude(), location.getLatitude());
        return WeatherMapper.mapFrom(weather, location);
    }

    @Override
    public List<LocationInfoDto> searchLocationsByQuery(String query) {
        try {
            String response = requestLocations(query);
            Type listType = new TypeToken<List<LocationInfoDto>>() {}.getType();
            return gson.fromJson(response, listType);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new CannotGetApiResponseException("The request cannot be completed", e);
        }
    }

    private String requestWeather(BigDecimal longitude, BigDecimal latitude) throws URISyntaxException, IOException, InterruptedException {
        String uri = host + "data/2.5/weather?" +
                           "lat=" + latitude.toString() +
                           "&lon=" + longitude.toString() +
                           "&units=metric" +
                           "&appid=" + appid;
        return doRequest(uri);
    }

    private String requestLocations(String query) throws URISyntaxException, IOException, InterruptedException {
        String queryParam = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String uri = host + "geo/1.0/direct?" +
                           "limit=0" +
                           "&q=" + queryParam +
                           "&appid=" + appid;
        return doRequest(uri);
    }

    public String doRequest(String uriString) throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URI(uriString);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            log.error(response);
            throw new CannotGetApiResponseException("Weather service is currently unavailable");
        }
        return response.body();
    }
}
