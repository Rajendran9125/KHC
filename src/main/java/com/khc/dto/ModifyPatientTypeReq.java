package com.khc.dto;

import lombok.Data;

@Data
public class ModifyPatientTypeReq {
    private Long id; // OpRegistration ID
    private String patientType;
    private String reason;
}
