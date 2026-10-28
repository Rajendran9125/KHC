package com.khc.dto;

import lombok.Data;

@Data
public class RefundReq {
    private Long billId;
    private Long itemId; // Specific line item ID (optional if full bill refund)
    private String reason; // e.g. "Service skipped / Doctor cancelled / Patient request"
}
