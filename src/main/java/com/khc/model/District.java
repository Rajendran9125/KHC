package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_district")
public class District extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "district_name")
    private String districtName;

    @OneToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "is_active")
    private Boolean isActive;

}
