package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_country")
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone_code")
    private String phoneCode;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
