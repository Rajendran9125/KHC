package com.khc.service;

import com.khc.model.OpBill;
import com.khc.model.OpBillItem;
import com.khc.model.OpRegistration;
import com.khc.repository.OpBillItemRepo;
import com.khc.repository.OpBillRepo;
import com.khc.repository.OpRegistrationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private OpBillRepo billRepo;

    @Autowired
    private OpBillItemRepo billItemRepo;

    @Autowired
    private OpRegistrationRepo registrationRepo;

    public Map<String, Object> getOpBillingReport(String startDate, String endDate, Long doctorId) {
        List<OpBill> bills = billRepo.findAllByOrderByBillDateDesc();

        if (startDate != null && !startDate.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            bills = bills.stream()
                    .filter(b -> b.getBillDate() != null && !b.getBillDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDate != null && !endDate.isEmpty()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            bills = bills.stream()
                    .filter(b -> b.getBillDate() != null && !b.getBillDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        if (doctorId != null) {
            bills = bills.stream()
                    .filter(b -> b.getDoctor() != null && doctorId.equals(b.getDoctor().getId()))
                    .collect(Collectors.toList());
        }

        double totalAmount = bills.stream().mapToDouble(b -> b.getTotalAmount() != null ? b.getTotalAmount() : 0.0).sum();
        double totalRefunded = bills.stream().mapToDouble(b -> b.getRefundedAmount() != null ? b.getRefundedAmount() : 0.0).sum();
        double totalPaid = bills.stream().mapToDouble(b -> Math.max(0.0, (b.getPaidAmount() != null ? b.getPaidAmount() : 0.0) - (b.getRefundedAmount() != null ? b.getRefundedAmount() : 0.0))).sum();
        double totalDiscount = bills.stream().mapToDouble(b -> b.getDiscount() != null ? b.getDiscount() : 0.0).sum();

        Map<String, Object> report = new HashMap<>();
        report.put("bills", bills);
        report.put("totalCount", bills.size());
        report.put("totalAmount", totalAmount);
        report.put("totalRefunded", totalRefunded);
        report.put("totalPaid", totalPaid); // Net Collected after deducting refunds
        report.put("totalDiscount", totalDiscount);
        return report;
    }

    public Map<String, Object> getRevenueReport(String startDate, String endDate) {
        List<OpBill> bills = billRepo.findAllByOrderByBillDateDesc();

        if (startDate != null && !startDate.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            bills = bills.stream()
                    .filter(b -> b.getBillDate() != null && !b.getBillDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDate != null && !endDate.isEmpty()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            bills = bills.stream()
                    .filter(b -> b.getBillDate() != null && !b.getBillDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        double totalBilled = bills.stream().mapToDouble(b -> b.getTotalAmount() != null ? b.getTotalAmount() : 0.0).sum();
        double totalRefunded = bills.stream().mapToDouble(b -> b.getRefundedAmount() != null ? b.getRefundedAmount() : 0.0).sum();
        double totalPaid = bills.stream().mapToDouble(b -> Math.max(0.0, (b.getPaidAmount() != null ? b.getPaidAmount() : 0.0) - (b.getRefundedAmount() != null ? b.getRefundedAmount() : 0.0))).sum();
        double totalDiscount = bills.stream().mapToDouble(b -> b.getDiscount() != null ? b.getDiscount() : 0.0).sum();

        // Payment type breakdown net of refunds
        Map<String, Double> paymentTypeBreakdown = bills.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getPaymentType() != null ? b.getPaymentType() : "OTHER",
                        Collectors.summingDouble(b -> Math.max(0.0, (b.getPaidAmount() != null ? b.getPaidAmount() : 0.0) - (b.getRefundedAmount() != null ? b.getRefundedAmount() : 0.0)))
                ));

        // Service-wise breakdown (excluding refunded items)
        Map<String, Double> serviceWiseRevenue = new HashMap<>();
        Map<String, Integer> serviceWiseCount = new HashMap<>();

        for (OpBill bill : bills) {
            if (bill.getItems() != null) {
                for (OpBillItem item : bill.getItems()) {
                    if ("REFUNDED".equalsIgnoreCase(item.getStatus())) continue;
                    String cat = item.getServiceCategory() != null ? item.getServiceCategory() : "GENERAL";
                    serviceWiseRevenue.put(cat, serviceWiseRevenue.getOrDefault(cat, 0.0) + (item.getTotalPrice() != null ? item.getTotalPrice() : 0.0));
                    serviceWiseCount.put(cat, serviceWiseCount.getOrDefault(cat, 0) + (item.getQuantity() != null ? item.getQuantity() : 1));
                }
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("totalBilled", totalBilled);
        report.put("totalRefunded", totalRefunded);
        report.put("totalPaid", totalPaid); // Net Realized Revenue
        report.put("totalDiscount", totalDiscount);
        report.put("billCount", bills.size());
        report.put("paymentTypeBreakdown", paymentTypeBreakdown);
        report.put("serviceWiseRevenue", serviceWiseRevenue);
        report.put("serviceWiseCount", serviceWiseCount);
        return report;
    }

    public Map<String, Object> getOpRegistrationReport(String startDate, String endDate, String visitType, Long doctorId) {
        List<OpRegistration> list = registrationRepo.findAllByOrderByRegistrationDateDesc();

        if (startDate != null && !startDate.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            list = list.stream()
                    .filter(r -> r.getRegistrationDate() != null && !r.getRegistrationDate().isBefore(start))
                    .collect(Collectors.toList());
        }

        if (endDate != null && !endDate.isEmpty()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            list = list.stream()
                    .filter(r -> r.getRegistrationDate() != null && !r.getRegistrationDate().isAfter(end))
                    .collect(Collectors.toList());
        }

        if (visitType != null && !visitType.isEmpty()) {
            list = list.stream()
                    .filter(r -> visitType.equalsIgnoreCase(r.getVisitType()))
                    .collect(Collectors.toList());
        }

        if (doctorId != null) {
            list = list.stream()
                    .filter(r -> r.getDoctor() != null && doctorId.equals(r.getDoctor().getId()))
                    .collect(Collectors.toList());
        }

        long newCount = list.stream().filter(r -> "NEW".equalsIgnoreCase(r.getVisitType())).count();
        long reviewCount = list.stream().filter(r -> "REVIEW".equalsIgnoreCase(r.getVisitType())).count();
        double totalFees = list.stream().mapToDouble(r -> r.getConsultationFee() != null ? r.getConsultationFee() : 0.0).sum();

        Map<String, Long> doctorCounts = list.stream()
                .filter(r -> r.getDoctor() != null)
                .collect(Collectors.groupingBy(r -> r.getDoctor().getDoctorName(), Collectors.counting()));

        Map<String, Long> patientTypeCounts = list.stream()
                .filter(r -> r.getPatientType() != null)
                .collect(Collectors.groupingBy(OpRegistration::getPatientType, Collectors.counting()));

        Map<String, Object> report = new HashMap<>();
        report.put("registrations", list);
        report.put("totalCount", list.size());
        report.put("newCount", newCount);
        report.put("reviewCount", reviewCount);
        report.put("totalFees", totalFees);
        report.put("doctorCounts", doctorCounts);
        report.put("patientTypeCounts", patientTypeCounts);
        return report;
    }
}
