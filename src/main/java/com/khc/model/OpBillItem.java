package com.khc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_op_bill_item")
public class OpBillItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonBackReference
    private OpBill bill;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_category")
    private String serviceCategory; // CONSULTATION, LAB, RADIOLOGY, PROCEDURE, PHARMACY

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "unit_price")
    private Double unitPrice = 0.0;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;
}
