package com.horolofi.rwa.dto;

import lombok.Builder;
import lombok.Data;
import com.horolofi.rwa.entity.OrderType;
import com.horolofi.rwa.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderBookItemDto {
    private Long orderId;
    private String userAddress;
    private OrderType orderType;
    private Integer quantity;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private long queuePosition;
}