package com.cofisweak.mapper;

import com.cofisweak.dto.WeatherDto;
import com.cofisweak.dto.openweather.WeatherItem;
import com.cofisweak.dto.openweather.WeatherResponseDto;
import com.cofisweak.model.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public static WeatherDto mapFrom(WeatherResponseDto weatherResponse, Location location) {
        WeatherItem weather = weatherResponse.weather().get(0);

        long unixTime = weatherResponse.dt() + weatherResponse.timezone();
        Instant instant = Instant.ofEpochSecond(unixTime);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("GMT"));
        String time = formatter.format(zonedDateTime);

        double temperature = weatherResponse.main().temp();
        double feelsLike = weatherResponse.main().feels_like();

        return WeatherDto.builder()
                .name(location.getName())
                .state(location.getState())
                .weatherName(weather.main())
                .description(weather.description())
                .icon(weather.icon())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .temperature(decimalFormat.format(temperature))
                .feelsLikeTemperature(decimalFormat.format(feelsLike))
                .time(time)
                .build();
    }
}
