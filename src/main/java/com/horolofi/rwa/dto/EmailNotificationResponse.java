package com.horolofi.rwa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationResponse {
    
    private String message;
    private Integer totalEmailsSent;
    private Boolean success;
}

