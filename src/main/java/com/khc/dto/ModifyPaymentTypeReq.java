package com.khc.dto;

import lombok.Data;

@Data
public class ModifyPaymentTypeReq {
    private Long id; // OpRegistration ID or OpBill ID
    private String paymentType;
    private String reason;
}
