package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_patient_type")
public class PatientType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName; // GENERAL, VIP, EMERGENCY, INSURANCE, CORPORATE

    @Column(name = "discount_percentage")
    private Double discountPercentage = 0.0;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
