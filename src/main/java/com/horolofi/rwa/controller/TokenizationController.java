package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.entity.AssetStatus;
import com.horolofi.rwa.service.TokenizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rwa")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TokenizationController {

    private final TokenizationService tokenizationService;

    @PostMapping("/request")
    public ResponseEntity<TokenizationResponseDto> requestTokenization(
            @Valid @RequestBody TokenizationRequestDto request) {
        
        log.info("Received tokenization request for brand: {}, model: {}", 
                request.getBrand(), request.getModel());

        TokenizationResponseDto response = tokenizationService.requestTokenization(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<TokenizationResponseDto>> getUserRequests(@RequestParam String userId) {
        List<TokenizationResponseDto> requests = tokenizationService.getUserTokenizationRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/admin/audit-list")
    public ResponseEntity<List<TokenizationResponseDto>> getAssetsForAudit(
            @RequestParam(required = false) String status) {
        
        AssetStatus assetStatus = null;
        
        if (status != null && !status.trim().isEmpty()) {
            try {
                // Konversi manual agar case-insensitive (misal: "approved" -> APPROVED)
                assetStatus = AssetStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        }
        
        // Jika status null, service akan menangani default-nya (misal: PENDING)
        List<TokenizationResponseDto> assets = tokenizationService.getAssetsForAudit(assetStatus);
        return ResponseEntity.ok(assets);
    }
}