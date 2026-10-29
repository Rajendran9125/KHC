package com.khc.dto;

import lombok.Data;

@Data
public class CountryDto {
    private Integer id;
    private String countryName;
    private String countryCode;
    private String phoneCode;
    private Boolean isActive;
}
