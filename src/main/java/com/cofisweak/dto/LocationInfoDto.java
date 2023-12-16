package com.cofisweak.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationInfoDto {
    @SerializedName("name")
    private String city;
    private String country;
    @SerializedName("lon")
    private BigDecimal longitude;
    @SerializedName("lat")
    private BigDecimal latitude;
    private String state;
}
