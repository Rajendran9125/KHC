package com.khc.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Long id;
    private String doctorName;
    private String specialization;
    private String department;
    private Double consultationFee;
    private String phone;
    private String roomNo;
    private Boolean isActive;
}
