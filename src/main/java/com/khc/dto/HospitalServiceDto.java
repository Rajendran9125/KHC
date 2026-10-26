package com.khc.dto;

import lombok.Data;

@Data
public class HospitalServiceDto {
    private Long id;
    private String serviceName;
    private String header; // ECG, LABTEST, CBC, RADIOLOGY, PROCEDURE, CONSULTATION
    private String department;
    private Double opRate;
    private Double ipRate;
    private Boolean isActive;
}
