package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.ApproveAssetRequestDto;
import com.horolofi.rwa.dto.AssetDetailResponseDto;
import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.entity.AssetStatus;
import com.horolofi.rwa.service.TokenizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TokenizationResponseDto> requestTokenization(
            @ModelAttribute @Valid TokenizationRequestDto request) {
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

        @PutMapping("admin/{assetId}/approve")
    public ResponseEntity<TokenizationResponseDto> approveAsset(
            @PathVariable Long assetId,
            @RequestBody ApproveAssetRequestDto request) {
        TokenizationResponseDto response = tokenizationService.approveAsset(assetId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("assets/{assetId}")
    public ResponseEntity<AssetDetailResponseDto> getAssetDetail(@PathVariable Long assetId) {
        AssetDetailResponseDto assetDetail = tokenizationService.getAssetDetail(assetId);
        return ResponseEntity.ok(assetDetail);
    }
}