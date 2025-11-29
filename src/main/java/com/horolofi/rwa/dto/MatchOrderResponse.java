package com.horolofi.rwa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchOrderResponse {
    private String status;
    private MatchData match_data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchData {
        private Long order_id;
        private String maker_address;
        private String maker_signature_data;
        private String maker_expiry;
        private String maker_nonce;
        private String quantity;
        private String price;
        private String fee; // Biaya transaksi atau administrasi
        private String totalPrice; // Total harga keseluruhan (biasanya price * quantity)
    }
}