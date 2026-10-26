package com.khc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResp {
    private Long id;
    private String username;
    private String fullName;
    private String role; // "ADMIN" or "USER"
    private String department;
    private List<String> allowedMenus;
}
