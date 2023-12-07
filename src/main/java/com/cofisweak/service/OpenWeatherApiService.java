package com.cofisweak.service;

import com.cofisweak.deserializer.GsonManager;
import com.cofisweak.dto.CityInfoDto;
import com.cofisweak.dto.WeatherInfoDto;
import com.cofisweak.exception.CannotDeserializeWeatherInfoException;
import com.cofisweak.exception.CannotGetApiResponseException;
import com.cofisweak.util.PropertiesUtil;
import com.cofisweak.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenWeatherApiService {
    private static final OpenWeatherApiService INSTANCE = new OpenWeatherApiService();
    private final Gson gson = GsonManager.getInstance();

    public List<CityInfoDto> searchWeatherListByCityName(String name) throws CannotGetApiResponseException {
        try {
            URI uri = buildSearchUri(name);
            HttpResponse<String> response = Utils.getResponse(uri);
            String content = response.body();
            Type listType = new TypeToken<List<CityInfoDto>>(){}.getType();
            return gson.fromJson(content, listType);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new CannotGetApiResponseException();
        }
    }

    public Optional<WeatherInfoDto> searchWeatherInfoByCoordinates(BigDecimal longitude, BigDecimal latitude) throws CannotGetApiResponseException {
        try {
            URI uri = buildGetWeatherInfoUri(longitude, latitude);
            HttpResponse<String> response = Utils.getResponse(uri);
            String content = response.body();

            return Optional.ofNullable(gson.fromJson(content, WeatherInfoDto.class));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new CannotGetApiResponseException();
        } catch (CannotDeserializeWeatherInfoException e) {
            return Optional.empty();
        }
        //TODO limiter, error handler, exception handler
    }

    private URI buildGetWeatherInfoUri(BigDecimal longitude, BigDecimal latitude) throws URISyntaxException {
        String host = PropertiesUtil.get("openweather.host");
        String appid = PropertiesUtil.get("openweather.appid");
        String uri = host + "data/2.5/weather?lat=" + latitude.toString() + "&lon=" + longitude.toString() + "&units=metric&appid=" + appid;
        return new URI(uri);
    }

    private URI buildSearchUri(String query) throws URISyntaxException {
        String host = PropertiesUtil.get("openweather.host");
        String appid = PropertiesUtil.get("openweather.appid");
        String uri = host + "geo/1.0/direct?&limit=0&q=" + query + "&appid=" + appid;
        return new URI(uri);
    }

    public static OpenWeatherApiService getInstance() {
        return INSTANCE;
    }
}
