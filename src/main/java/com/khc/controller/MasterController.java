package com.khc.controller;

import com.khc.dto.ApiResponse;
import com.khc.dto.DoctorDto;
import com.khc.dto.HospitalServiceDto;
import com.khc.dto.UserDto;
import com.khc.model.*;
import com.khc.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    // --- Users (USER CREATION) ---
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<AppUser>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok(masterService.getAllUsers()));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<AppUser>> createUser(@RequestBody UserDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("User created successfully", masterService.createUser(dto)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<AppUser>> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("User updated successfully", masterService.updateUser(id, dto)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        masterService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully", null));
    }

    // --- System Menus (from DB table) ---
    @GetMapping("/menus")
    public ResponseEntity<ApiResponse<List<AppMenu>>> getAllMenus() {
        return ResponseEntity.ok(ApiResponse.ok(masterService.getAllMenus()));
    }

    // --- Services (SERVICE MASTER: Service Name, Header, Department, OPRATE, IPRATE, Status) ---
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<HospitalService>>> getAllServices(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        List<HospitalService> list = activeOnly ? masterService.getActiveServices() : masterService.getAllServices(query);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping("/services")
    public ResponseEntity<ApiResponse<HospitalService>> createService(@RequestBody HospitalServiceDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Hospital service tariff created successfully", masterService.createService(dto)));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ApiResponse<HospitalService>> updateService(@PathVariable Long id, @RequestBody HospitalServiceDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Hospital service tariff updated successfully", masterService.updateService(id, dto)));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        masterService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.ok("Service tariff deleted successfully", null));
    }

    // --- Doctors (ADMIN) ---
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<Doctor>>> getAllDoctors(@RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        List<Doctor> doctors = activeOnly ? masterService.getActiveDoctors() : masterService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.ok(doctors));
    }

    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<Doctor>> createDoctor(@RequestBody DoctorDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor saved successfully", masterService.createDoctor(dto)));
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<Doctor>> updateDoctor(@PathVariable Long id, @RequestBody DoctorDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Doctor updated successfully", masterService.updateDoctor(id, dto)));
    }

    // --- Patient & Payment Types ---
    @GetMapping("/patient-types")
    public ResponseEntity<ApiResponse<List<PatientType>>> getPatientTypes() {
        return ResponseEntity.ok(ApiResponse.ok(masterService.getAllPatientTypes()));
    }

    @GetMapping("/payment-types")
    public ResponseEntity<ApiResponse<List<PaymentType>>> getPaymentTypes() {
        return ResponseEntity.ok(ApiResponse.ok(masterService.getAllPaymentTypes()));
    }

    // --- Admin Dashboard Stats ---
    @GetMapping("/admin/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminStats() {
        return ResponseEntity.ok(ApiResponse.ok(masterService.getAdminDashboardStats()));
    }
}
