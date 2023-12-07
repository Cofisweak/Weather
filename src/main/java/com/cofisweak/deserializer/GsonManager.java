package com.cofisweak.deserializer;

import com.cofisweak.dto.WeatherInfoDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonManager {
    private static final Gson INSTANCE;

    static {
        INSTANCE = new GsonBuilder()
                .registerTypeAdapter(WeatherInfoDto.class, new WeatherInfoDeserializer())
                .create();
    }

    public static Gson getInstance() {
        return INSTANCE;
    }

}
