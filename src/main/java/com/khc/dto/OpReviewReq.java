package com.khc.dto;

import lombok.Data;

@Data
public class OpReviewReq {
    private Long patientId;
    private String mrn;
    private Long doctorId;
    private String patientType;
    private String paymentType;
    private Double consultationFee;
    private String department;
    private String remarks;
}
