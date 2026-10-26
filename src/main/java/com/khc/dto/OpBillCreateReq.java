package com.khc.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpBillCreateReq {
    private Long opRegistrationId;
    private Long patientId;
    private Long doctorId;
    private String paymentType; // CASH, CARD, UPI, INSURANCE
    private Double discount;
    private Double tax;
    private Double paidAmount;
    private String remarks;
    private List<OpBillItemReq> items;
}
