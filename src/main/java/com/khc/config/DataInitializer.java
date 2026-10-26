package com.khc.config;

import com.khc.model.*;
import com.khc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void run(String... args) throws Exception {
        if (doctorRepo.count() > 0) {
            return; // Data already initialized
        }

        System.out.println(">>> Initializing KHC-HIMS Demo Seed Data...");

        // 1. Seed Doctors
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

        // 2. Seed Users with Passwords and Roles
        AppUser u1 = new AppUser();
        u1.setUsername("admin");
        u1.setPassword("admin123");
        u1.setFullName("Hospital Administrator");
        u1.setEmail("admin@khc.org");
        u1.setRole("ADMIN");
        u1.setDepartment("Administration");
        u1.setPhone("9811122233");
        u1.setAllowedMenus("all");
        u1.setIsActive(true);
        userRepo.save(u1);

        AppUser u2 = new AppUser();
        u2.setUsername("user1");
        u2.setPassword("user123");
        u2.setFullName("Kavita Sundaram (Front Desk)");
        u2.setEmail("kavita@khc.org");
        u2.setRole("USER");
        u2.setDepartment("Front Desk");
        u2.setPhone("9822233344");
        u2.setAllowedMenus("op-new,op-review,patient-update,op-billing,report-registration");
        u2.setIsActive(true);
        userRepo.save(u2);

        AppUser u3 = new AppUser();
        u3.setUsername("cashier1");
        u3.setPassword("cashier123");
        u3.setFullName("Ramesh Varma (Cashier)");
        u3.setEmail("ramesh@khc.org");
        u3.setRole("USER");
        u3.setDepartment("Accounts & Billing");
        u3.setPhone("9833344455");
        u3.setAllowedMenus("op-billing,modify-payment-type-bill,report-billing,report-revenue");
        u3.setIsActive(true);
        userRepo.save(u3);

        // 3. Seed Hospital Services (Tariff Master: Service Name, Header, Department, OPRATE, IPRATE, isActive)
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

        // 4. Seed Patient Types
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

        // 5. Seed Payment Types
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

        // 6. Seed Sample Patients
        Patient p1 = new Patient();
        p1.setMrn("MRN-2026-1001");
        p1.setFirstName("Rahul");
        p1.setLastName("Sharma");
        p1.setAge(35);
        p1.setGender("MALE");
        p1.setMobile("9876000001");
        p1.setEmail("rahul.sharma@example.com");
        p1.setAddress("42 MG Road, Bangalore");
        p1.setBloodGroup("O+");
        p1.setEmergencyContact("9876000002");
        patientRepo.save(p1);

        Patient p2 = new Patient();
        p2.setMrn("MRN-2026-1002");
        p2.setFirstName("Sunita");
        p2.setLastName("Deshmukh");
        p2.setAge(28);
        p2.setGender("FEMALE");
        p2.setMobile("9876000003");
        p2.setEmail("sunita.d@example.com");
        p2.setAddress("15 Park Street, Pune");
        p2.setBloodGroup("B+");
        p2.setEmergencyContact("9876000004");
        patientRepo.save(p2);

        Patient p3 = new Patient();
        p3.setMrn("MRN-2026-1003");
        p3.setFirstName("Mohammad");
        p3.setLastName("Faiz");
        p3.setAge(52);
        p3.setGender("MALE");
        p3.setMobile("9876000005");
        p3.setEmail("m.faiz@example.com");
        p3.setAddress("88 Lake View, Hyderabad");
        p3.setBloodGroup("A+");
        p3.setEmergencyContact("9876000006");
        patientRepo.save(p3);

        // 7. Seed Sample Registrations
        OpRegistration op1 = new OpRegistration();
        op1.setOpNumber("OP-2026-2001");
        op1.setPatient(p1);
        op1.setDoctor(d1);
        op1.setVisitType("NEW");
        op1.setPatientType("GENERAL");
        op1.setPaymentType("CASH");
        op1.setDepartment("Cardiology");
        op1.setConsultationFee(500.0);
        op1.setStatus("BILLED");
        op1.setRemarks("Routine heart checkup");
        op1.setRegistrationDate(LocalDateTime.now().minusHours(4));
        registrationRepo.save(op1);

        OpRegistration op2 = new OpRegistration();
        op2.setOpNumber("OP-2026-2002");
        op2.setPatient(p2);
        op2.setDoctor(d2);
        op2.setVisitType("NEW");
        op2.setPatientType("VIP");
        op2.setPaymentType("UPI");
        op2.setDepartment("General Medicine");
        op2.setConsultationFee(300.0);
        op2.setStatus("WAITING");
        op2.setRemarks("Fever and cold for 2 days");
        op2.setRegistrationDate(LocalDateTime.now().minusHours(2));
        registrationRepo.save(op2);

        OpRegistration op3 = new OpRegistration();
        op3.setOpNumber("OP-2026-2003");
        op3.setPatient(p3);
        op3.setDoctor(d3);
        op3.setVisitType("REVIEW");
        op3.setPatientType("INSURANCE");
        op3.setPaymentType("CARD");
        op3.setDepartment("Orthopedics");
        op3.setConsultationFee(225.0);
        op3.setStatus("BILLED");
        op3.setRemarks("Post knee surgery 2-week review");
        op3.setRegistrationDate(LocalDateTime.now().minusHours(1));
        registrationRepo.save(op3);

        // 8. Seed Sample OP Bills
        OpBill b1 = new OpBill();
        b1.setBillNumber("BILL-2026-3001");
        b1.setOpRegistration(op1);
        b1.setPatient(p1);
        b1.setDoctor(d1);
        b1.setBillDate(LocalDateTime.now().minusHours(3));
        b1.setPaymentType("CASH");
        b1.setSubTotal(1700.0);
        b1.setDiscount(100.0);
        b1.setTax(80.0);
        b1.setTotalAmount(1680.0);
        b1.setPaidAmount(1680.0);
        b1.setPaymentStatus("PAID");
        b1.setRemarks("Cardiology consultation & baseline diagnostics");

        List<OpBillItem> items1 = new ArrayList<>();
        OpBillItem i1 = new OpBillItem();
        i1.setBill(b1);
        i1.setServiceName("Specialist Cardiology Consultation");
        i1.setServiceCategory("CONSULTATION");
        i1.setQuantity(1);
        i1.setUnitPrice(500.0);
        i1.setTotalPrice(500.0);
        items1.add(i1);

        OpBillItem i2 = new OpBillItem();
        i2.setBill(b1);
        i2.setServiceName("12-Lead Electrocardiogram (ECG)");
        i2.setServiceCategory("PROCEDURE");
        i2.setQuantity(1);
        i2.setUnitPrice(400.0);
        i2.setTotalPrice(400.0);
        items1.add(i2);

        OpBillItem i3 = new OpBillItem();
        i3.setBill(b1);
        i3.setServiceName("Lipid Profile Screen");
        i3.setServiceCategory("LAB");
        i3.setQuantity(1);
        i3.setUnitPrice(800.0);
        i3.setTotalPrice(800.0);
        items1.add(i3);

        b1.setItems(items1);
        billRepo.save(b1);

        OpBill b2 = new OpBill();
        b2.setBillNumber("BILL-2026-3002");
        b2.setOpRegistration(op3);
        b2.setPatient(p3);
        b2.setDoctor(d3);
        b2.setBillDate(LocalDateTime.now().minusMinutes(45));
        b2.setPaymentType("CARD");
        b2.setSubTotal(1125.0);
        b2.setDiscount(50.0);
        b2.setTax(50.0);
        b2.setTotalAmount(1125.0);
        b2.setPaidAmount(1125.0);
        b2.setPaymentStatus("PAID");
        b2.setRemarks("Orthopedic review & X-ray knee");

        List<OpBillItem> items2 = new ArrayList<>();
        OpBillItem i4 = new OpBillItem();
        i4.setBill(b2);
        i4.setServiceName("Review Consultation Fee");
        i4.setServiceCategory("CONSULTATION");
        i4.setQuantity(1);
        i4.setUnitPrice(225.0);
        i4.setTotalPrice(225.0);
        items2.add(i4);

        OpBillItem i5 = new OpBillItem();
        i5.setBill(b2);
        i5.setServiceName("Digital Chest X-Ray (PA View)");
        i5.setServiceCategory("RADIOLOGY");
        i5.setQuantity(1);
        i5.setUnitPrice(900.0);
        i5.setTotalPrice(900.0);
        items2.add(i5);

        b2.setItems(items2);
        billRepo.save(b2);

        System.out.println(">>> Demo Seed Data Initialized Successfully!");
    }
}
