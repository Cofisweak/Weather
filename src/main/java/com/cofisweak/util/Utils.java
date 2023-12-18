package com.cofisweak.util;

import com.cofisweak.dto.WeatherCardActionButtonType;
import com.cofisweak.dto.WeatherDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static boolean isFieldNotFilled(String field) {
        return field == null || field.isBlank();
    }

    public static boolean isInvalidPassword(String username) {
        return !username.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,20}$");
    }

    public static void redirectToMainPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }

    public static void setButtonTypesForWeatherList(List<WeatherDto> list, WeatherCardActionButtonType buttonType) {
        list.forEach(weatherDto -> weatherDto.setButtonType(buttonType));
    }
}
