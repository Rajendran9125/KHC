package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_op_registration")
public class OpRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "op_number", unique = true, nullable = false)
    private String opNumber; // e.g. OP2001

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "visit_type", nullable = false)
    private String visitType; // "NEW" or "REVIEW"

    @Column(name = "patient_type")
    private String patientType; // GENERAL, VIP, INSURANCE, CORPORATE, etc.

    @Column(name = "payment_type")
    private String paymentType; // CASH, UPI, CARD, INSURANCE

    @Column(name = "department")
    private String department;

    @Column(name = "consultation_fee")
    private Double consultationFee = 0.0;

    @Column(name = "status")
    private String status = "WAITING"; // WAITING, CONSULTED, BILLED, CANCELLED

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "residential_address")
    private String residentialAddress;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
        if (this.registrationDate == null) {
            this.registrationDate = LocalDateTime.now();
        }
    }
}
