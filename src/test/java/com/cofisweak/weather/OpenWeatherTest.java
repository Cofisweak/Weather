package com.cofisweak.weather;

import com.cofisweak.dto.LocationInfoDto;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.exception.CannotGetApiResponseException;
import com.cofisweak.model.Location;
import com.cofisweak.service.weather.OpenWeatherApiService;
import com.cofisweak.service.weather.WeatherService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

public class OpenWeatherTest {

    WeatherService weatherService;

    @Mock
    HttpClient httpClient;

    @Mock
    HttpResponse<Object> httpResponse;

    Location exampleLocation = Location.builder()
            .latitude(new BigDecimal("53.9024716"))
            .longitude(new BigDecimal("27.5618225"))
            .name("Minsk, BY")
            .build();

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(httpResponse);
        Gson gson = new Gson();
        weatherService = new OpenWeatherApiService(gson, httpClient);
    }

    @Test
    void whenSearchLocationsByQueryThenReturnCorrectList() throws IOException {
        String response = readResponseFromResourceFile("search_locations_response.txt");
        Mockito.when(httpResponse.body()).thenReturn(response);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);

        List<LocationInfoDto> result = weatherService.searchLocationsByQuery("Minsk");

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(locationInfoDto -> locationInfoDto.getCity().equals("Minsk")));
    }

    @Test
    void whenGetWeatherByLocationThenReturnCorrectWeatherDto() throws IOException {
        String response = readResponseFromResourceFile("get_weather_response.txt");
        Mockito.when(httpResponse.body()).thenReturn(response);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);

        WeatherDto result = weatherService.getWeatherByLocation(exampleLocation);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(exampleLocation.getName(), result.getName());
        Assertions.assertEquals(exampleLocation.getLongitude(), result.getLongitude());
        Assertions.assertEquals(exampleLocation.getLatitude(), result.getLatitude());
        Assertions.assertNotNull(result.getIcon());
        Assertions.assertNotNull(result.getTime());
        Assertions.assertNotNull(result.getDescription());
        Assertions.assertNotNull(result.getFeelsLikeTemperature());
        Assertions.assertNotNull(result.getWeatherName());
        Assertions.assertNotNull(result.getTemperature());
    }

    @Test
    void whenApiReturn429StatusCodeThenThrowsException() {
        Mockito.when(httpResponse.statusCode()).thenReturn(429);
        Assertions.assertThrows(CannotGetApiResponseException.class, () -> weatherService.getWeatherByLocation(exampleLocation));
        Assertions.assertThrows(CannotGetApiResponseException.class, () -> weatherService.searchLocationsByQuery(""));
    }

    @Test
    void whenApiReturn500StatusCodeThenThrowsException() {
        Mockito.when(httpResponse.statusCode()).thenReturn(500);
        Assertions.assertThrows(CannotGetApiResponseException.class, () -> weatherService.getWeatherByLocation(exampleLocation));
        Assertions.assertThrows(CannotGetApiResponseException.class, () -> weatherService.searchLocationsByQuery(""));
    }

    @Test
    void whenApiReturn503StatusCodeThenThrowsException() {
        Mockito.when(httpResponse.statusCode()).thenReturn(503);
        Assertions.assertThrows(CannotGetApiResponseException.class, () -> weatherService.getWeatherByLocation(exampleLocation));
        Assertions.assertThrows(CannotGetApiResponseException.class, () -> weatherService.searchLocationsByQuery(""));
    }

    private String readResponseFromResourceFile(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
            assert inputStream != null;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                while (bufferedReader.ready()) {
                    stringBuilder.append(bufferedReader.readLine());
                }
            }
        }
        return stringBuilder.toString();
    }
}
