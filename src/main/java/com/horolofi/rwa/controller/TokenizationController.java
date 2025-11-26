package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.service.TokenizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
}