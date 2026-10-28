package com.khc.service;

import com.khc.dto.OpNewRegistrationReq;
import com.khc.dto.OpReviewReq;
import com.khc.dto.PatientUpdateReq;
import com.khc.model.Doctor;
import com.khc.model.OpRegistration;
import com.khc.model.Patient;
import com.khc.repository.DoctorRepo;
import com.khc.repository.OpRegistrationRepo;
import com.khc.repository.PatientRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RegistrationService {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private OpRegistrationRepo registrationRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    private static final AtomicLong mrnCounter = new AtomicLong(1);
    private static final AtomicLong opCounter = new AtomicLong(2001);

    @PostConstruct
    public void initCounters() {
        try {
            String currentYear = String.valueOf(LocalDate.now().getYear());
            String prefix = "MRN" + currentYear;
            long maxSeq = 0;
            List<Patient> patients = patientRepo.findAll();
            for (Patient p : patients) {
                if (p.getMrn() != null) {
                    String clean = p.getMrn().trim();
                    if (clean.startsWith(prefix)) {
                        try {
                            String numPart = clean.substring(prefix.length());
                            long val = Long.parseLong(numPart);
                            if (val > maxSeq) {
                                maxSeq = val;
                            }
                        } catch (Exception ignored) {}
                    }
                }
            }
            mrnCounter.set(maxSeq + 1);
        } catch (Exception e) {
            mrnCounter.set(1);
        }

        try {
            long maxOp = 2000;
            List<OpRegistration> ops = registrationRepo.findAll();
            for (OpRegistration o : ops) {
                if (o.getOpNumber() != null) {
                    String clean = o.getOpNumber().trim();
                    if (clean.startsWith("OP") && !clean.contains("-")) {
                        try {
                            long val = Long.parseLong(clean.substring(2));
                            if (val > maxOp) maxOp = val;
                        } catch (Exception ignored) {}
                    }
                }
            }
            opCounter.set(maxOp + 1);
        } catch (Exception e) {
            opCounter.set(2001);
        }
    }

    @Transactional
    public OpRegistration registerOpNew(OpNewRegistrationReq req) {
        Doctor doctor = doctorRepo.findById(req.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + req.getDoctorId()));

        // Create new Patient
        Patient patient = new Patient();
        String year = String.valueOf(LocalDate.now().getYear());
        long seq = mrnCounter.getAndIncrement();
        patient.setMrn("MRN" + year + String.format("%03d", seq));
        patient.setFirstName(req.getFirstName());
        patient.setLastName(req.getLastName());
        patient.setDob(req.getDob());
        patient.setAge(req.getAge());
        patient.setGender(req.getGender() != null ? req.getGender() : "MALE");
        patient.setMobile(req.getMobile());
        patient.setEmail(req.getEmail());
        patient.setAddress(req.getAddress());
        patient.setBloodGroup(req.getBloodGroup());
        patient.setEmergencyContact(req.getEmergencyContact());
        patient = patientRepo.save(patient);

        // Create OP Registration
        OpRegistration op = new OpRegistration();
        op.setOpNumber("OP" + opCounter.getAndIncrement());
        op.setPatient(patient);
        op.setDoctor(doctor);
        op.setVisitType("NEW");
        op.setPatientType(req.getPatientType() != null ? req.getPatientType() : "GENERAL");
        op.setPaymentType(req.getPaymentType() != null ? req.getPaymentType() : "CASH");
        op.setDepartment(req.getDepartment() != null ? req.getDepartment() : doctor.getDepartment());
        op.setConsultationFee(req.getConsultationFee() != null ? req.getConsultationFee() : doctor.getConsultationFee());
        op.setStatus("WAITING");
        op.setRemarks(req.getRemarks());
        op.setRegistrationDate(LocalDateTime.now());

        return registrationRepo.save(op);
    }

    @Transactional
    public OpRegistration registerOpReview(OpReviewReq req) {
        Patient patient;
        if (req.getPatientId() != null) {
            patient = patientRepo.findById(req.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + req.getPatientId()));
        } else if (req.getMrn() != null && !req.getMrn().trim().isEmpty()) {
            patient = patientRepo.findByMrn(req.getMrn().trim())
                    .orElseThrow(() -> new RuntimeException("Patient not found with MRN: " + req.getMrn()));
        } else {
            throw new IllegalArgumentException("Either patientId or MRN is required for review visit");
        }

        Doctor doctor = doctorRepo.findById(req.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + req.getDoctorId()));

        OpRegistration op = new OpRegistration();
        op.setOpNumber("OP" + opCounter.getAndIncrement());
        op.setPatient(patient);
        op.setDoctor(doctor);
        op.setVisitType("REVIEW");
        op.setPatientType(req.getPatientType() != null ? req.getPatientType() : "GENERAL");
        op.setPaymentType(req.getPaymentType() != null ? req.getPaymentType() : "CASH");
        op.setDepartment(req.getDepartment() != null ? req.getDepartment() : doctor.getDepartment());
        // Review consultation fee usually 50% or nominal
        Double fee = req.getConsultationFee() != null ? req.getConsultationFee() : (doctor.getConsultationFee() != null ? doctor.getConsultationFee() * 0.5 : 100.0);
        op.setConsultationFee(fee);
        op.setStatus("WAITING");
        op.setRemarks(req.getRemarks() != null ? req.getRemarks() : "Follow-up / Review Visit");
        op.setRegistrationDate(LocalDateTime.now());

        return registrationRepo.save(op);
    }

    @Transactional
    public Patient updatePatientDetail(PatientUpdateReq req) {
        Patient patient = patientRepo.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + req.getId()));

        if (req.getFirstName() != null) patient.setFirstName(req.getFirstName());
        if (req.getLastName() != null) patient.setLastName(req.getLastName());
        if (req.getDob() != null) patient.setDob(req.getDob());
        if (req.getAge() != null) patient.setAge(req.getAge());
        if (req.getGender() != null) patient.setGender(req.getGender());
        if (req.getMobile() != null) patient.setMobile(req.getMobile());
        if (req.getEmail() != null) patient.setEmail(req.getEmail());
        if (req.getAddress() != null) patient.setAddress(req.getAddress());
        if (req.getBloodGroup() != null) patient.setBloodGroup(req.getBloodGroup());
        if (req.getEmergencyContact() != null) patient.setEmergencyContact(req.getEmergencyContact());

        return patientRepo.save(patient);
    }

    @Transactional
    public OpRegistration modifyRegistrationDoctor(Long opId, Long newDoctorId, String reason) {
        OpRegistration op = registrationRepo.findById(opId)
                .orElseThrow(() -> new RuntimeException("OP Registration not found with id: " + opId));
        Doctor newDoctor = doctorRepo.findById(newDoctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + newDoctorId));

        op.setDoctor(newDoctor);
        op.setDepartment(newDoctor.getDepartment());
        if (reason != null && !reason.trim().isEmpty()) {
            op.setRemarks((op.getRemarks() != null ? op.getRemarks() + " | " : "") + "Doctor changed: " + reason);
        }
        return registrationRepo.save(op);
    }

    @Transactional
    public OpRegistration modifyRegistrationPatientType(Long opId, String newType, String reason) {
        OpRegistration op = registrationRepo.findById(opId)
                .orElseThrow(() -> new RuntimeException("OP Registration not found with id: " + opId));

        op.setPatientType(newType);
        if (reason != null && !reason.trim().isEmpty()) {
            op.setRemarks((op.getRemarks() != null ? op.getRemarks() + " | " : "") + "Patient Type changed: " + reason);
        }
        return registrationRepo.save(op);
    }

    @Transactional
    public OpRegistration modifyRegistrationPaymentType(Long opId, String newPaymentType, String reason) {
        OpRegistration op = registrationRepo.findById(opId)
                .orElseThrow(() -> new RuntimeException("OP Registration not found with id: " + opId));

        op.setPaymentType(newPaymentType);
        if (reason != null && !reason.trim().isEmpty()) {
            op.setRemarks((op.getRemarks() != null ? op.getRemarks() + " | " : "") + "Payment Type changed: " + reason);
        }
        return registrationRepo.save(op);
    }

    public List<OpRegistration> getAllRegistrations() {
        return registrationRepo.findAllByOrderByRegistrationDateDesc();
    }

    public List<OpRegistration> searchRegistrations(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllRegistrations();
        }
        return registrationRepo.searchRegistrations(query.trim());
    }

    public List<Patient> searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            return patientRepo.findAll();
        }
        return patientRepo.searchPatients(query.trim());
    }

    public OpRegistration getRegistrationById(Long id) {
        return registrationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OP Registration not found with id: " + id));
    }
}
