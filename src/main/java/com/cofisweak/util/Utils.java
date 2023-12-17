package com.cofisweak.util;

import com.cofisweak.dto.WeatherCardActionButtonType;
import com.cofisweak.dto.WeatherDto;
import com.cofisweak.exception.CannotGetApiResponseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static TemplateEngine getTemplateEngine(HttpServletRequest req) {
        return (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
    }
    public static boolean isFieldNotFilled(String field) {
        return field == null || field.isBlank();
    }

    public static boolean isInvalidPassword(String username) {
        return !username.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,20}$");
    }

    public static void redirectToMainPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }

    public static HttpResponse<String> doRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new CannotGetApiResponseException("Weather service is currently unavailable");
        }
        return response;
    }

    public static String getResponseContent(String uriString) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(uriString);
        HttpResponse<String> response = Utils.doRequest(uri);
        return response.body();
    }

    public static void setButtonTypesForWeatherList(List<WeatherDto> list, WeatherCardActionButtonType buttonType) {
        list.forEach(weatherDto -> weatherDto.setButtonType(buttonType));
    }
}
