package com.khc.dto;

import lombok.Data;

@Data
public class ResetPasswordReq {
    private String username;
    private String newPassword;
    private String emailOrPhone;
}
