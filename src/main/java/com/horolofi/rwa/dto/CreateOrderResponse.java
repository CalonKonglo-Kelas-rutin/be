package com.horolofi.rwa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    
    private Long orderId;
    private String walletAddress;
    private String orderType;
    private Integer assetId;
    private String assetName;
    private BigDecimal price;
    private Integer quantity;
    private Double fee;
    private Double totalPrice;
    private String status; // PENDING, MATCHED, CANCELLED
    private LocalDateTime createdAt;
    private String message;
}