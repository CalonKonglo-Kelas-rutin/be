package com.horolofi.rwa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationRequest {
    
    @NotBlank(message = "Serial number wajib diisi")
    private String serialNumber;
    
    @NotBlank(message = "Tanggal voting wajib diisi")
    private String votingDate;
}

