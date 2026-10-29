package com.khc.dto;

import lombok.Data;

@Data
public class StateDto {
    private Integer id;
    private String stateName;
    private String stateCode;
    private Integer countryId;
    private Boolean isActive;
}
