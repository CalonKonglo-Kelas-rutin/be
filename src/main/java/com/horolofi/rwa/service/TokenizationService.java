package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;

import java.util.List;

public interface TokenizationService {
    TokenizationResponseDto requestTokenization(TokenizationRequestDto request);
    List<TokenizationResponseDto> getUserTokenizationRequests(String userId);
}