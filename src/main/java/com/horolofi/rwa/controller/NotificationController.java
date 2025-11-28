package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.EmailNotificationRequest;
import com.horolofi.rwa.dto.EmailNotificationResponse;
import com.horolofi.rwa.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "API untuk ngirim notifikasi email ke users")
public class NotificationController {
    
    private final EmailService emailService;
    
    @PostMapping("/sendEmailNotif")
    @Operation(summary = "Kirim email notifikasi voting ke holders token", 
               description = "Kirim email ke semua user yang nemegang token dengan serial number tertentu buat ngasih tau ada voting")
    public ResponseEntity<EmailNotificationResponse> sendEmailNotif(
            @Valid @RequestBody EmailNotificationRequest request) {
        
        int emailsSent = emailService.sendEmailNotif(
            request.getSerialNumber(), 
            request.getVotingDate()
        );
        
        if (emailsSent == 0) {
            return ResponseEntity.ok(EmailNotificationResponse.builder()
                .message("Ga ada user yang nemegang token dengan serial number tersebut")
                .totalEmailsSent(0)
                .success(false)
                .build());
        }
        
        return ResponseEntity.ok(EmailNotificationResponse.builder()
            .message("Email notifikasi berhasil dikirim")
            .totalEmailsSent(emailsSent)
            .success(true)
            .build());
    }
}

