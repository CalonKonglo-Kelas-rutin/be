package com.horolofi.rwa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;
    
    @Column(name = "asset_name")
    private String assetName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "total_value")
    private Double totalValue;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

