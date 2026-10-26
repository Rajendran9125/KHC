package com.khc.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_id")
    private Integer createdId;

    @Column(name = "modify_id")
    private Integer modifyId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    // Backward compatibility for snake_case callers
    public LocalDateTime getModify_date() {
        return modifyDate;
    }

    public void setModify_date(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
        if (this.modifyDate == null) {
            this.modifyDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modifyDate = LocalDateTime.now();
    }

}
