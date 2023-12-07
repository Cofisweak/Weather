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
public class WeatherInfoDto {
    private String city;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String country;
    private String weatherName;
    private String description;
    private String icon;
    private Double temperature;
    private Double feelsLikeTemperature;
}
