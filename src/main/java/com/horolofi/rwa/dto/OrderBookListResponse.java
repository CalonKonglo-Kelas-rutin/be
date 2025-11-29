package com.horolofi.rwa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderBookListResponse {
    private String status;
    private Meta meta;
    private List<OrderBookItemDto> data;

    @Data
    @Builder
    public static class Meta {
        private long totalQueue;
        private String assetId;
    }
}