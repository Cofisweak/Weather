package com.cofisweak.dto.openweather;

import java.util.List;

public record WeatherResponseDto(int timezone,
                                 Long dt,
                                 List<WeatherItem> weather,
                                 Main main) {
}
