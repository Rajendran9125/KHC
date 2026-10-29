package com.khc.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientUpdateReq {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Integer age;
    private String gender;
    private String mobile;
    private String email;
    private String address;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private String pincode;
    private String bloodGroup;
    private String emergencyContact;
}
