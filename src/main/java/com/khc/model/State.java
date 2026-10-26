package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_state")
public class State extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "state_name")
    private String stateName;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "is_active")
    private Boolean isActive;
}
