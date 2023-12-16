package com.cofisweak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherDto {
    private long id;

    private String name;
    private String state;

    private String weatherName;
    private String description;
    private String icon;
    private String time;

    private BigDecimal longitude;
    private BigDecimal latitude;

    private String temperature;
    private String feelsLikeTemperature;

    private WeatherCardActionButtonType buttonType;
}
