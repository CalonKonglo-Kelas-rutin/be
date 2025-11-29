package com.horolofi.rwa.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String walletAddress;
    private String email;
    private String username;
}