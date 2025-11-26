package com.horolofi.rwa.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ApproveAssetRequestDto {
    private String documentsUrl;
    private String auditorNotes;
    private BigDecimal appraisedValueUsd;
    private String ipfsMetadataUri;
    private String tokenId;
    private String txHashMint;
}