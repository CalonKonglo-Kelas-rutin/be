package com.horolofi.rwa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.horolofi.rwa.entity.AssetStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetDetailResponseDto {
    private Integer id;
    private String ownerId;
    private String brand;
    private String model;
    private String refNumber;
    private String serialNumber;
    private Integer productionYear;
    private String conditionRating;
    private Boolean hasBox;
    private Boolean hasPapers;
    private String imageUrls;
    private String documentsUrl;
    private AssetStatus status;
    private String auditorNotes;
    private BigDecimal appraisedValueUsd;
    private String ipfsMetadataUri;
    private String tokenId;
    private String txHashMint;
    private LocalDateTime createdAt;
    private LocalDateTime approveAt;
    private LocalDateTime rejectedAt;
}