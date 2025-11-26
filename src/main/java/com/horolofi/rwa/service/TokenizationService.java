package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.ApproveAssetRequestDto;
import com.horolofi.rwa.dto.AssetDetailResponseDto;
import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.entity.AssetStatus;

import java.util.List;

public interface TokenizationService {
    TokenizationResponseDto requestTokenization(TokenizationRequestDto request);
    List<TokenizationResponseDto> getUserTokenizationRequests(String userId);
    List<TokenizationResponseDto> getAssetsForAudit(AssetStatus status);
    TokenizationResponseDto approveAsset(Long assetId, ApproveAssetRequestDto request);
    AssetDetailResponseDto getAssetDetail(Long assetId);
}