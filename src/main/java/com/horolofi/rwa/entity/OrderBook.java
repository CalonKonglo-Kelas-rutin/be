package com.horolofi.rwa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;
    
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "price")
    private Double price;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

