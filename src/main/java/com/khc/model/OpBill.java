package com.khc.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_op_bill")
public class OpBill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_number", unique = true, nullable = false)
    private String billNumber; // e.g. BILL-2026-0001

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "op_registration_id")
    private OpRegistration opRegistration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "bill_date")
    private LocalDateTime billDate;

    @Column(name = "payment_type")
    private String paymentType; // CASH, CARD, UPI, INSURANCE

    @Column(name = "sub_total")
    private Double subTotal = 0.0;

    @Column(name = "discount")
    private Double discount = 0.0;

    @Column(name = "tax")
    private Double tax = 0.0;

    @Column(name = "total_amount")
    private Double totalAmount = 0.0;

    @Column(name = "paid_amount")
    private Double paidAmount = 0.0;

    @Column(name = "payment_status")
    private String paymentStatus = "PAID"; // PAID, PARTIAL, UNPAID

    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OpBillItem> items = new ArrayList<>();

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
        if (this.billDate == null) {
            this.billDate = LocalDateTime.now();
        }
    }
}
