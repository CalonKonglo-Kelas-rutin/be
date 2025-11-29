package com.horolofi.rwa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID; // Tambahkan import ini

@Entity
@Table(name = "assets", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;    

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "ref_number")
    private String refNumber;

    @Column(name = "serial_number", unique = false)
    private String serialNumber;

    @Column(name = "production_year")
    private Integer productionYear;

    @Column(name = "condition_rating")
    private String conditionRating;

    @Column(name = "has_box")
    private Boolean hasBox;

    @Column(name = "has_papers")
    private Boolean hasPapers;

    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls; // Store as JSON string or comma-separated

    @Column(name = "documents_url", columnDefinition = "TEXT")
    private String documentsUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssetStatus status = AssetStatus.PENDING;

    @Column(name = "auditor_notes", columnDefinition = "TEXT")
    private String auditorNotes;

    @Column(name = "appraised_value_usd", precision = 15, scale = 2)
    private BigDecimal appraisedValueUsd;

    @Column(name = "ipfs_metadata_uri")
    private String ipfsMetadataUri;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "tx_hash_mint")
    private String txHashMint;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}