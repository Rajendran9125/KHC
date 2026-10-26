package com.khc.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Basedto {

    private Integer createdId;

    private Integer modifyId;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime modifyDate = LocalDateTime.now();

    // Backward compatibility for snake_case callers
    public LocalDateTime getModify_date() {
        return modifyDate;
    }

    public void setModify_date(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }
}
