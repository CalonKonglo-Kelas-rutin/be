package com.horolofi.rwa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTokenAddressRequestDto {
    @NotBlank(message = "Token address is required")
    private String tokenAddress;
}