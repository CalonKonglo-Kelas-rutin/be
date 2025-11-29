package com.horolofi.rwa.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderBookItemDto {
    private Long orderId;
    private String userAddress;
    private Integer quantity;
    private LocalDateTime createdAt;
    private long queuePosition;
}