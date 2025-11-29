package com.horolofi.rwa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_book", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID unik untuk setiap entri order book
    
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset; // Aset yang diperdagangkan dalam order ini
          
    @Column(name = "order_type", nullable = false)
    private OrderType orderType; // Jenis order (misalnya: BUY atau SELL)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // Status order (misalnya: OPEN, FILLED, CANCELLED)

    @ManyToOne
    @JoinColumn(name = "maker_address", nullable = false)
    private User maker_address; // User yang membuat order (Maker)

    @ManyToOne
    @JoinColumn(name = "taker_address", nullable = true)
    private User taker_address; // User yang mengambil/mengeksekusi order (Taker)
    
    @Column(name = "price")
    private Double price; // Harga per unit aset
    
    @Column(name = "fee")
    private Double fee; // Biaya transaksi atau administrasi

    @Column(name = "total_price")
    private Double totalPrice; // Total harga keseluruhan (biasanya price * quantity)

    @Column(name = "quantity")
    private Integer quantity; // Jumlah unit aset yang dipesan

    @Column(name = "signature_data", columnDefinition = "TEXT")
    private String signatureData; // Data tanda tangan digital untuk verifikasi keamanan
    
    @Column(name = "tx_hash", columnDefinition = "TEXT" )
    private String txHash; // Hash transaksi blockchain terkait order ini

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Waktu ketika order dibuat
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Waktu ketika order terakhir diperbarui
}

