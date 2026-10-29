package com.khc.dto;

import lombok.Data;

@Data
public class CityDto {
    private Integer id;
    private String cityName;
    private Integer stateId;
    private String defaultPincode;
    private Boolean isActive;
}
