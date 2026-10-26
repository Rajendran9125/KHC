package com.khc.dto;

import lombok.Data;

@Data
public class ModifyDoctorReq {
    private Long id; // OpRegistration ID or OpBill ID
    private Long doctorId;
    private String reason;
}
