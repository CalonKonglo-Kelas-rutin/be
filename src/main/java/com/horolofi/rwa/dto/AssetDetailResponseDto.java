package com.horolofi.rwa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetDetailResponseDto {
    private Integer id;
    private String brand;
    private String model;
    private String serialNumber;
    private String conditionRating;
    private String status;
}