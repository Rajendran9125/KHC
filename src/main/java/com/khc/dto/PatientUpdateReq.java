package com.khc.dto;

import lombok.Data;

@Data
public class PatientUpdateReq {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String mobile;
    private String email;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
}
