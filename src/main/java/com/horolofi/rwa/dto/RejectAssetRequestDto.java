package com.horolofi.rwa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectAssetRequestDto {
    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;
}