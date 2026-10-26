package com.khc.dto;

import com.khc.model.Country;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StateSaveReq extends Basedto {
    private String stateName;
    private Country country;
    private Boolean isActive;
}
