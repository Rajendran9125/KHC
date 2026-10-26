package com.khc.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role; // ADMIN, USER, RECEPTIONIST, CASHIER, DOCTOR
    private String department;
    private String phone;
    private String allowedMenus;
    private Boolean isActive;
}
