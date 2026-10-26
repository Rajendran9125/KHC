package com.khc.dto;

import lombok.Data;

@Data
public class OpBillItemReq {
    private String serviceName;
    private String serviceCategory; // CONSULTATION, LAB, RADIOLOGY, PROCEDURE, PHARMACY
    private Integer quantity;
    private Double unitPrice;
}
