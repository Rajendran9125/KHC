package com.khc.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "khc_app_menu")
public class AppMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_code", unique = true, nullable = false)
    private String menuCode; // e.g. "admin", "users", "op-new", etc.

    @Column(name = "menu_label", nullable = false)
    private String menuLabel; // e.g. "MASTER: ADMIN", "REGISTRATION: OP NEW"

    @Column(name = "category")
    private String category; // "MASTER", "REGISTRATION", "BILLING", "REPORT"

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
