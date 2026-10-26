package com.khc.controller;

import com.khc.dto.ApiResponse;
import com.khc.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // 1. OP BILLING REPORT
    @GetMapping("/op-billing")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOpBillingReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.getOpBillingReport(startDate, endDate, doctorId)));
    }

    // 2. REVENUE (SUMMARY + SERVICE WISE) REPORT
    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.getRevenueReport(startDate, endDate)));
    }

    // 3. OP REGISTRATION REPORT
    @GetMapping("/op-registration")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOpRegistrationReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String visitType,
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.getOpRegistrationReport(startDate, endDate, visitType, doctorId)));
    }
}
