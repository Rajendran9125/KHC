package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_hospital_service")
public class HospitalService extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName; // e.g. "Complete Blood Count (CBC)", "12-Lead ECG"

    @Column(name = "header", nullable = false)
    private String header; // e.g. "ECG", "LABTEST", "CBC", "RADIOLOGY", "PROCEDURE", "CONSULTATION"

    @Column(name = "department")
    private String department; // e.g. "Pathology", "Cardiology", "Radiology", "General Medicine"

    @Column(name = "op_rate", nullable = false)
    private Double opRate = 0.0; // Outpatient Rate

    @Column(name = "ip_rate", nullable = false)
    private Double ipRate = 0.0; // Inpatient Rate

    @Column(name = "is_active")
    private Boolean isActive = true;
}
