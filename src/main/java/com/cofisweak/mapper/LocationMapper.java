package com.cofisweak.mapper;

import com.cofisweak.dto.LocationInfoDto;
import com.cofisweak.model.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {
    public static Location mapFrom(LocationInfoDto dto) {
        String name = "%s, %s".formatted(dto.getCity(), dto.getCountry());
        return Location.builder()
                .name(name)
                .state(dto.getState())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }
}
