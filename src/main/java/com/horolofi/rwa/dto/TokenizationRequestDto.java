package com.horolofi.rwa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenizationRequestDto {

    @NotNull(message = "Owner ID is required")
    private String ownerId;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;
    private String refNumber;
    private String serialNumber;
    private Integer productionYear;
    private String conditionRating;
    private Boolean hasBox;
    private Boolean hasPapers;

    private List<String> imageUrls;
    private List<String> documentsUrl;
    
    // Tambahan untuk upload
    private MultipartFile image;
}