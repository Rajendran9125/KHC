package com.khc.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OpNewRegistrationReq {
    // Patient details
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Integer age;
    private String gender; // MALE, FEMALE, OTHER
    private String mobile;
    private String email;
    private String address;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private String pincode;
    private String residentialAddress;
    private String bloodGroup;
    private String emergencyContact;

    // OP Visit details
    private Long doctorId;
    private String patientType; // GENERAL, VIP, INSURANCE, CORPORATE
    private String paymentType; // CASH, CARD, UPI
    private Double consultationFee;
    private String department;
    private String remarks;
}
