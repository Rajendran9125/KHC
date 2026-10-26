package com.khc.service;

import com.khc.dto.DoctorDto;
import com.khc.dto.HospitalServiceDto;
import com.khc.dto.LoginResp;
import com.khc.dto.UserDto;
import com.khc.model.*;
import com.khc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MasterService {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientTypeRepo patientTypeRepo;

    @Autowired
    private PaymentTypeRepo paymentTypeRepo;

    @Autowired
    private HospitalServiceRepo serviceRepo;

    @Autowired
    private OpRegistrationRepo registrationRepo;

    @Autowired
    private OpBillRepo billRepo;

    // --- Users ---
    public List<AppUser> getAllUsers() {
        return userRepo.findAll();
    }

    public AppUser createUser(UserDto dto) {
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
        }
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword() != null && !dto.getPassword().trim().isEmpty() ? dto.getPassword() : "password123");
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER");
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhone());
        user.setAllowedMenus(dto.getAllowedMenus() != null ? dto.getAllowedMenus() : "");
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return userRepo.save(user);
    }

    public AppUser updateUser(Long id, UserDto dto) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) user.setPassword(dto.getPassword());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getDepartment() != null) user.setDepartment(dto.getDepartment());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getAllowedMenus() != null) user.setAllowedMenus(dto.getAllowedMenus());
        if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    // --- User Authentication / Login ---
    public LoginResp login(String username, String password) {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (user.getIsActive() != null && !user.getIsActive()) {
            throw new RuntimeException("Account is inactive. Contact Administrator.");
        }

        // Check password (supports plain text or preset default)
        if (user.getPassword() != null && !user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        List<String> allowedMenusList = new ArrayList<>();
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            // ADMIN has ALL rights
            allowedMenusList = Arrays.asList(
                    "admin", "users", "services",
                    "op-new", "op-review", "patient-update",
                    "modify-doctor-reg", "modify-patient-type", "modify-payment-type-reg",
                    "op-billing", "modify-doctor-bill", "modify-payment-type-bill",
                    "report-billing", "report-revenue", "report-registration"
            );
        } else {
            // USER has selected menus only
            if (user.getAllowedMenus() != null && !user.getAllowedMenus().trim().isEmpty()) {
                String[] menus = user.getAllowedMenus().split(",");
                for (String m : menus) {
                    if (!m.trim().isEmpty()) allowedMenusList.add(m.trim());
                }
            } else {
                // Default staff/user menus: Registration & Billing
                allowedMenusList = Arrays.asList(
                        "op-new", "op-review", "patient-update",
                        "op-billing", "report-registration"
                );
            }
        }

        return new LoginResp(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.getDepartment(),
                allowedMenusList
        );
    }

    // --- Hospital Services (Tariff & Amount Master) ---
    public List<HospitalService> getAllServices(String query) {
        if (query != null && !query.trim().isEmpty()) {
            return serviceRepo.searchServices(query.trim());
        }
        return serviceRepo.findAll();
    }

    public List<HospitalService> getActiveServices() {
        return serviceRepo.findByIsActiveTrue();
    }

    public HospitalService createService(HospitalServiceDto dto) {
        HospitalService service = new HospitalService();
        service.setServiceName(dto.getServiceName());
        service.setHeader(dto.getHeader() != null ? dto.getHeader().toUpperCase() : "GENERAL");
        service.setDepartment(dto.getDepartment());
        service.setOpRate(dto.getOpRate() != null ? dto.getOpRate() : 0.0);
        service.setIpRate(dto.getIpRate() != null ? dto.getIpRate() : 0.0);
        service.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return serviceRepo.save(service);
    }

    public HospitalService updateService(Long id, HospitalServiceDto dto) {
        HospitalService service = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        if (dto.getServiceName() != null) service.setServiceName(dto.getServiceName());
        if (dto.getHeader() != null) service.setHeader(dto.getHeader().toUpperCase());
        if (dto.getDepartment() != null) service.setDepartment(dto.getDepartment());
        if (dto.getOpRate() != null) service.setOpRate(dto.getOpRate());
        if (dto.getIpRate() != null) service.setIpRate(dto.getIpRate());
        if (dto.getIsActive() != null) {
            service.setIsActive(dto.getIsActive());
        }
        return serviceRepo.save(service);
    }

    public void deleteService(Long id) {
        serviceRepo.deleteById(id);
    }

    // --- Doctors ---
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public List<Doctor> getActiveDoctors() {
        return doctorRepo.findByIsActiveTrue();
    }

    public Doctor createDoctor(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setDoctorName(dto.getDoctorName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setDepartment(dto.getDepartment());
        doctor.setConsultationFee(dto.getConsultationFee() != null ? dto.getConsultationFee() : 200.0);
        doctor.setPhone(dto.getPhone());
        doctor.setRoomNo(dto.getRoomNo());
        doctor.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return doctorRepo.save(doctor);
    }

    public Doctor updateDoctor(Long id, DoctorDto dto) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        if (dto.getDoctorName() != null) doctor.setDoctorName(dto.getDoctorName());
        if (dto.getSpecialization() != null) doctor.setSpecialization(dto.getSpecialization());
        if (dto.getDepartment() != null) doctor.setDepartment(dto.getDepartment());
        if (dto.getConsultationFee() != null) doctor.setConsultationFee(dto.getConsultationFee());
        if (dto.getPhone() != null) doctor.setPhone(dto.getPhone());
        if (dto.getRoomNo() != null) doctor.setRoomNo(dto.getRoomNo());
        if (dto.getIsActive() != null) doctor.setIsActive(dto.getIsActive());
        return doctorRepo.save(doctor);
    }

    // --- Patient & Payment Types ---
    public List<PatientType> getAllPatientTypes() {
        return patientTypeRepo.findAll();
    }

    public List<PaymentType> getAllPaymentTypes() {
        return paymentTypeRepo.findAll();
    }

    // --- Admin Dashboard Summary ---
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDoctors", doctorRepo.count());
        stats.put("totalUsers", userRepo.count());
        stats.put("totalServices", serviceRepo.count());
        stats.put("totalRegistrations", registrationRepo.count());
        stats.put("totalBills", billRepo.count());
        Double revenue = billRepo.sumPaidRevenue();
        stats.put("totalRevenue", revenue != null ? revenue : 0.0);
        return stats;
    }
}
