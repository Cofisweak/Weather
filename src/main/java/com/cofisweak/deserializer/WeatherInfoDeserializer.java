package com.cofisweak.deserializer;

import com.cofisweak.dto.WeatherInfoDto;
import com.cofisweak.exception.CannotDeserializeWeatherInfoException;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class WeatherInfoDeserializer implements JsonDeserializer<WeatherInfoDto> {
    @Override
    public WeatherInfoDto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws CannotDeserializeWeatherInfoException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();

            String city = jsonObject.getAsJsonPrimitive("name").getAsString();

            JsonObject sys = jsonObject.getAsJsonObject("sys");
            String country = sys.getAsJsonPrimitive("country").getAsString();

            JsonObject coordinates = jsonObject.getAsJsonObject("coord");
            String longitude = coordinates.getAsJsonPrimitive("lon").getAsString();
            String latitude = coordinates.getAsJsonPrimitive("lat").getAsString();

            JsonObject main = jsonObject.getAsJsonObject("main");
            String temperature = main.getAsJsonPrimitive("temp").getAsString();
            String feelsLikeTemperature = main.getAsJsonPrimitive("feels_like").getAsString();

            JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
            JsonObject weather = weatherArray.get(0).getAsJsonObject();
            String weatherName = weather.getAsJsonPrimitive("main").getAsString();
            String description = weather.getAsJsonPrimitive("description").getAsString();
            String icon = weather.getAsJsonPrimitive("icon").getAsString();

            return WeatherInfoDto.builder()
                    .city(city)
                    .country(country)
                    .latitude(new BigDecimal(longitude))
                    .longitude(new BigDecimal(latitude))
                    .temperature(Double.parseDouble(temperature))
                    .feelsLikeTemperature(Double.parseDouble(feelsLikeTemperature))
                    .weatherName(weatherName)
                    .description(description)
                    .icon(icon)
                    .build();
        } catch (Exception e) {
            throw new CannotDeserializeWeatherInfoException();
        }
    }
}
