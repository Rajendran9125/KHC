package com.khc.controller;

import com.khc.dto.ApiResponse;
import com.khc.dto.ModifyDoctorReq;
import com.khc.dto.ModifyPaymentTypeReq;
import com.khc.dto.OpBillCreateReq;
import com.khc.model.OpBill;
import com.khc.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    // 1. OP SERVICE BILLING
    @PostMapping("/op-service")
    public ResponseEntity<ApiResponse<OpBill>> createOpServiceBill(@RequestBody OpBillCreateReq req) {
        OpBill bill = billingService.createOpServiceBill(req);
        return ResponseEntity.ok(ApiResponse.ok("OP Service Bill created successfully", bill));
    }

    // 2. DOCTOR NAME MODIFY - OP BILL
    @PutMapping("/modify-doctor")
    public ResponseEntity<ApiResponse<OpBill>> modifyDoctor(@RequestBody ModifyDoctorReq req) {
        OpBill bill = billingService.modifyBillDoctor(req.getId(), req.getDoctorId(), req.getReason());
        return ResponseEntity.ok(ApiResponse.ok("Doctor modified successfully on Bill: " + bill.getBillNumber(), bill));
    }

    // 3. PAYMENT TYPE MODIFY
    @PutMapping("/modify-payment-type")
    public ResponseEntity<ApiResponse<OpBill>> modifyPaymentType(@RequestBody ModifyPaymentTypeReq req) {
        OpBill bill = billingService.modifyBillPaymentType(req.getId(), req.getPaymentType(), req.getReason());
        return ResponseEntity.ok(ApiResponse.ok("Payment type modified successfully on Bill: " + bill.getBillNumber(), bill));
    }

    // List and details
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<OpBill>>> getBills(@RequestParam(required = false) String query) {
        List<OpBill> list = billingService.searchBills(query);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OpBill>> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(billingService.getBillById(id)));
    }
}
