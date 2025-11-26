package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;

public interface TokenizationService {
    TokenizationResponseDto requestTokenization(TokenizationRequestDto request);
}