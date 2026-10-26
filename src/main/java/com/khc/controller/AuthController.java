package com.khc.controller;

import com.khc.dto.ApiResponse;
import com.khc.dto.LoginReq;
import com.khc.dto.LoginResp;
import com.khc.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MasterService masterService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResp>> login(@RequestBody LoginReq req) {
        try {
            LoginResp resp = masterService.login(req.getUsername(), req.getPassword());
            return ResponseEntity.ok(ApiResponse.ok("Login successful", resp));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
