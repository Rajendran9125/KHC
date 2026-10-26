package com.khc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CountrySaveReq extends Basedto {
    private String countryName;
    private Boolean isActive;
}
