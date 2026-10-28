package com.khc.config;

import com.khc.model.*;
import com.khc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

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
    private PatientRepo patientRepo;

    @Autowired
    private OpRegistrationRepo registrationRepo;

    @Autowired
    private OpBillRepo billRepo;

    @Autowired
    private AppMenuRepo menuRepo;

    @Override
    public void run(String... args) throws Exception {
        // 1. Clean up sample transactional data (bills, registrations, patients)
        try {
            billRepo.deleteAll();
            registrationRepo.deleteAll();
            patientRepo.deleteAll();
        } catch (Exception e) {
            System.out.println("Note: Transaction cleanup: " + e.getMessage());
        }

        // 2. Remove demo non-admin users if present
        userRepo.findByUsername("user1").ifPresent(userRepo::delete);
        userRepo.findByUsername("cashier1").ifPresent(userRepo::delete);

        // 3. Ensure ONLY ONE Admin User Exists (admin / admin123)
        if (userRepo.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setFullName("KH Clinic Administrator");
            admin.setEmail("admin@khclinic.org");
            admin.setRole("ADMIN");
            admin.setDepartment("Administration");
            admin.setPhone("9811122233");
            admin.setAllowedMenus("all");
            admin.setIsActive(true);
            userRepo.save(admin);
            System.out.println(">>> Admin User (admin / admin123) initialized.");
        }

        // 4. Seed Essential Master Records (Doctors, Tariff Services, Patient Types, Payment Types)
        if (doctorRepo.count() == 0) {
            Doctor d1 = new Doctor();
            d1.setDoctorName("Dr. Rajesh Sharma");
            d1.setSpecialization("Cardiologist (MD, DM)");
            d1.setDepartment("Cardiology");
            d1.setConsultationFee(500.0);
            d1.setPhone("+91 98765 43210");
            d1.setRoomNo("OPD-101");
            d1.setIsActive(true);
            doctorRepo.save(d1);

            Doctor d2 = new Doctor();
            d2.setDoctorName("Dr. Priya Nair");
            d2.setSpecialization("General Physician (MBBS, MD)");
            d2.setDepartment("General Medicine");
            d2.setConsultationFee(300.0);
            d2.setPhone("+91 98765 43211");
            d2.setRoomNo("OPD-102");
            d2.setIsActive(true);
            doctorRepo.save(d2);

            Doctor d3 = new Doctor();
            d3.setDoctorName("Dr. Vikram Patel");
            d3.setSpecialization("Orthopedic Surgeon (MS)");
            d3.setDepartment("Orthopedics");
            d3.setConsultationFee(450.0);
            d3.setPhone("+91 98765 43212");
            d3.setRoomNo("OPD-103");
            d3.setIsActive(true);
            doctorRepo.save(d3);

            Doctor d4 = new Doctor();
            d4.setDoctorName("Dr. Ananya Sen");
            d4.setSpecialization("Pediatrician (MD Pediatrics)");
            d4.setDepartment("Pediatrics");
            d4.setConsultationFee(350.0);
            d4.setPhone("+91 98765 43213");
            d4.setRoomNo("OPD-104");
            d4.setIsActive(true);
            doctorRepo.save(d4);

            Doctor d5 = new Doctor();
            d5.setDoctorName("Dr. Arun Kumar");
            d5.setSpecialization("ENT Specialist (MS ENT)");
            d5.setDepartment("ENT");
            d5.setConsultationFee(400.0);
            d5.setPhone("+91 98765 43214");
            d5.setRoomNo("OPD-105");
            d5.setIsActive(true);
            doctorRepo.save(d5);
        }

        if (serviceRepo.count() == 0) {
            Object[][] serviceData = {
                    {"Complete Blood Count (CBC)", "CBC", "Pathology", 350.0, 450.0, true},
                    {"12-Lead Electrocardiogram (ECG)", "ECG", "Cardiology", 400.0, 550.0, true},
                    {"Random Blood Sugar (RBS)", "LABTEST", "Biochemistry", 100.0, 150.0, true},
                    {"Lipid Profile Screen", "LABTEST", "Biochemistry", 800.0, 1000.0, true},
                    {"Liver Function Test (LFT)", "LABTEST", "Biochemistry", 750.0, 950.0, true},
                    {"Serum Creatinine & Urea", "LABTEST", "Biochemistry", 350.0, 450.0, true},
                    {"Urine Routine & Microscopic", "LABTEST", "Clinical Pathology", 200.0, 300.0, true},
                    {"Digital Chest X-Ray (PA View)", "RADIOLOGY", "Radiology", 500.0, 700.0, true},
                    {"Ultrasound Whole Abdomen (USG)", "RADIOLOGY", "Radiology", 1200.0, 1600.0, true},
                    {"Wound Dressing & Suturing", "PROCEDURE", "OPD Minor OT", 350.0, 500.0, true},
                    {"IV Cannulation & Infusion", "PROCEDURE", "Nursing", 250.0, 350.0, true},
                    {"Emergency Nebulization", "PROCEDURE", "Emergency", 200.0, 300.0, true},
                    {"General OPD Consultation", "CONSULTATION", "General Medicine", 300.0, 500.0, true},
                    {"Specialist Cardiology Consultation", "CONSULTATION", "Cardiology", 500.0, 800.0, true},
                    {"Orthopedic Consultation", "CONSULTATION", "Orthopedics", 450.0, 700.0, true},
                    {"Pediatric Consultation", "CONSULTATION", "Pediatrics", 350.0, 550.0, true},
                    {"ENT Endoscopy Examination", "PROCEDURE", "ENT", 850.0, 1200.0, true},
            };

            for (Object[] s : serviceData) {
                HospitalService svc = new HospitalService();
                svc.setServiceName((String) s[0]);
                svc.setHeader((String) s[1]);
                svc.setDepartment((String) s[2]);
                svc.setOpRate((Double) s[3]);
                svc.setIpRate((Double) s[4]);
                svc.setIsActive((Boolean) s[5]);
                serviceRepo.save(svc);
            }
        }

        if (patientTypeRepo.count() == 0) {
            String[][] ptypes = {
                    {"GENERAL", "0.0"},
                    {"VIP", "10.0"},
                    {"EMERGENCY", "0.0"},
                    {"INSURANCE", "15.0"},
                    {"CORPORATE", "20.0"}
            };
            for (String[] pt : ptypes) {
                PatientType p = new PatientType();
                p.setTypeName(pt[0]);
                p.setDiscountPercentage(Double.parseDouble(pt[1]));
                p.setIsActive(true);
                patientTypeRepo.save(p);
            }
        }

        if (paymentTypeRepo.count() == 0) {
            String[][] paytypes = {
                    {"CASH", "Direct cash at counter"},
                    {"UPI", "GPay, PhonePe, Paytm QR"},
                    {"CARD", "Credit / Debit Card swipe"},
                    {"INSURANCE", "Third Party Insurance / TPA claim"},
                    {"CHEQUE", "Bank Demand Draft or Cheque"}
            };
            for (String[] pay : paytypes) {
                PaymentType p = new PaymentType();
                p.setTypeName(pay[0]);
                p.setDescription(pay[1]);
                p.setIsActive(true);
                paymentTypeRepo.save(p);
            }
        }

        // 5. Seed System Menus into DB Table (khc_app_menu) if empty
        if (menuRepo.count() == 0) {
            Object[][] menusData = {
                    {"admin", "MASTER: ADMIN", "MASTER", 1},
                    {"users", "MASTER: USER CREATION", "MASTER", 2},
                    {"services", "MASTER: SERVICE MASTER (TARIFF)", "MASTER", 3},
                    {"op-new", "REGISTRATION: OP NEW", "REGISTRATION", 4},
                    {"op-review", "REGISTRATION: OP REVIEW", "REGISTRATION", 5},
                    {"patient-update", "REGISTRATION: PATIENT DETAIL UPDATE", "REGISTRATION", 6},
                    {"modify-doctor-reg", "REGISTRATION: DOCTOR MODIFY", "REGISTRATION", 7},
                    {"modify-patient-type", "REGISTRATION: PATIENT TYPE MODIFY", "REGISTRATION", 8},
                    {"modify-payment-type-reg", "REGISTRATION: PAYMENT TYPE MODIFY", "REGISTRATION", 9},
                    {"op-billing", "BILLING: OP SERVICE BILLING", "BILLING", 10},
                    {"modify-doctor-bill", "BILLING: DOCTOR MODIFY", "BILLING", 11},
                    {"modify-payment-type-bill", "BILLING: PAYMENT TYPE MODIFY", "BILLING", 12},
                    {"refund-billing", "BILLING: REFUND BILLING (SERVICE REVERSAL)", "BILLING", 13},
                    {"report-billing", "REPORT: OP BILLING", "REPORT", 14},
                    {"report-revenue", "REPORT: REVENUE (SUMMARY + SERVICE WISE)", "REPORT", 15},
                    {"report-registration", "REPORT: OP REGISTRATION", "REPORT", 16}
            };
            for (Object[] m : menusData) {
                AppMenu menu = new AppMenu();
                menu.setMenuCode((String) m[0]);
                menu.setMenuLabel((String) m[1]);
                menu.setCategory((String) m[2]);
                menu.setDisplayOrder((Integer) m[3]);
                menu.setIsActive(true);
                menuRepo.save(menu);
            }
            System.out.println(">>> System Menus Table (khc_app_menu) Seeded Successfully.");
        } else {
            // Ensure refund-billing is present
            if (!menuRepo.existsByMenuCode("refund-billing")) {
                AppMenu menu = new AppMenu();
                menu.setMenuCode("refund-billing");
                menu.setMenuLabel("BILLING: REFUND BILLING (SERVICE REVERSAL)");
                menu.setCategory("BILLING");
                menu.setDisplayOrder(13);
                menu.setIsActive(true);
                menuRepo.save(menu);
            }
        }

        System.out.println(">>> KH CLINIC System Ready: Only 1 Admin user configured (admin / admin123). Sample data removed.");
    }
}
