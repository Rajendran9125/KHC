package com.khc.service;

import com.khc.dto.OpBillCreateReq;
import com.khc.dto.OpBillItemReq;
import com.khc.model.*;
import com.khc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BillingService {

    @Autowired
    private OpBillRepo billRepo;

    @Autowired
    private OpBillItemRepo billItemRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private OpRegistrationRepo registrationRepo;

    private static final AtomicLong billCounter = new AtomicLong(3001);

    @Transactional
    public OpBill createOpServiceBill(OpBillCreateReq req) {
        Patient patient = patientRepo.findById(req.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + req.getPatientId()));

        Doctor doctor = doctorRepo.findById(req.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + req.getDoctorId()));

        OpRegistration opRegistration = null;
        if (req.getOpRegistrationId() != null) {
            opRegistration = registrationRepo.findById(req.getOpRegistrationId()).orElse(null);
            if (opRegistration != null) {
                opRegistration.setStatus("BILLED");
                registrationRepo.save(opRegistration);
            }
        }

        String year = String.valueOf(LocalDate.now().getYear());
        OpBill bill = new OpBill();
        bill.setBillNumber("BILL-" + year + "-" + String.format("%04d", billCounter.getAndIncrement()));
        bill.setPatient(patient);
        bill.setDoctor(doctor);
        bill.setOpRegistration(opRegistration);
        bill.setBillDate(LocalDateTime.now());
        bill.setPaymentType(req.getPaymentType() != null ? req.getPaymentType() : "CASH");
        bill.setRemarks(req.getRemarks());

        double subTotal = 0.0;
        List<OpBillItem> billItems = new ArrayList<>();

        if (req.getItems() != null && !req.getItems().isEmpty()) {
            for (OpBillItemReq itemReq : req.getItems()) {
                OpBillItem item = new OpBillItem();
                item.setBill(bill);
                item.setServiceName(itemReq.getServiceName());
                item.setServiceCategory(itemReq.getServiceCategory() != null ? itemReq.getServiceCategory() : "PROCEDURE");
                int qty = itemReq.getQuantity() != null && itemReq.getQuantity() > 0 ? itemReq.getQuantity() : 1;
                double price = itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : 0.0;
                double total = qty * price;
                item.setQuantity(qty);
                item.setUnitPrice(price);
                item.setTotalPrice(total);
                subTotal += total;
                billItems.add(item);
            }
        }

        double discount = req.getDiscount() != null ? req.getDiscount() : 0.0;
        double tax = req.getTax() != null ? req.getTax() : 0.0;
        double totalAmount = Math.max(0.0, subTotal - discount + tax);
        double paidAmount = req.getPaidAmount() != null ? req.getPaidAmount() : totalAmount;

        bill.setSubTotal(subTotal);
        bill.setDiscount(discount);
        bill.setTax(tax);
        bill.setTotalAmount(totalAmount);
        bill.setPaidAmount(paidAmount);

        if (paidAmount >= totalAmount) {
            bill.setPaymentStatus("PAID");
        } else if (paidAmount > 0) {
            bill.setPaymentStatus("PARTIAL");
        } else {
            bill.setPaymentStatus("UNPAID");
        }

        bill.setItems(billItems);
        return billRepo.save(bill);
    }

    @Transactional
    public OpBill modifyBillDoctor(Long billId, Long newDoctorId, String reason) {
        OpBill bill = billRepo.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));
        Doctor newDoctor = doctorRepo.findById(newDoctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + newDoctorId));

        bill.setDoctor(newDoctor);
        if (reason != null && !reason.trim().isEmpty()) {
            bill.setRemarks((bill.getRemarks() != null ? bill.getRemarks() + " | " : "") + "Doctor changed: " + reason);
        }
        return billRepo.save(bill);
    }

    @Transactional
    public OpBill modifyBillPaymentType(Long billId, String newPaymentType, String reason) {
        OpBill bill = billRepo.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billId));

        bill.setPaymentType(newPaymentType);
        if (reason != null && !reason.trim().isEmpty()) {
            bill.setRemarks((bill.getRemarks() != null ? bill.getRemarks() + " | " : "") + "Payment Type changed: " + reason);
        }
        return billRepo.save(bill);
    }

    public List<OpBill> getAllBills() {
        return billRepo.findAllByOrderByBillDateDesc();
    }

    public List<OpBill> searchBills(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBills();
        }
        return billRepo.searchBills(query.trim());
    }

    public OpBill getBillById(Long id) {
        return billRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id: " + id));
    }
}
