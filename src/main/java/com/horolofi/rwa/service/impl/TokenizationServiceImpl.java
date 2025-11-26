package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.dto.ApproveAssetRequestDto;
import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.dto.AssetDetailResponseDto;
import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.entity.AssetStatus;
import com.horolofi.rwa.exception.AssetNotFoundException;
import com.horolofi.rwa.mapper.AssetMapper;
import com.horolofi.rwa.repository.AssetRepository;
import com.horolofi.rwa.service.TokenizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenizationServiceImpl implements TokenizationService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;

    @Override
    @Transactional
    public TokenizationResponseDto requestTokenization(TokenizationRequestDto request) {
        // Map DTO to Entity
        Asset asset = assetMapper.toEntity(request);

        // Save to database
        Asset savedAsset = assetRepository.save(asset);

        // Map Entity to Response DTO
        return assetMapper.toDto(savedAsset);
    }

    @Override
    public List<TokenizationResponseDto> getUserTokenizationRequests(String userId) {
        List<Asset> assets = assetRepository.findByOwnerId(userId);
        return assets.stream()
                .map(assetMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TokenizationResponseDto> getAssetsForAudit(AssetStatus status) {
        // Default ke PENDING jika status tidak dikirim oleh user
        AssetStatus targetStatus = (status != null) ? status : AssetStatus.PENDING;
        
        List<Asset> assets = assetRepository.findByStatus(targetStatus);
        return assets.stream()
                .map(assetMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TokenizationResponseDto approveAsset(Long assetId, ApproveAssetRequestDto request) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + assetId));

        // Update fields
        asset.setDocumentsUrl(request.getDocumentsUrl());
        asset.setAuditorNotes(request.getAuditorNotes());
        asset.setAppraisedValueUsd(request.getAppraisedValueUsd());
        asset.setIpfsMetadataUri(request.getIpfsMetadataUri());
        asset.setTokenId(request.getTokenId());
        asset.setTxHashMint(request.getTxHashMint());
        
        // Set status to APPROVED
        asset.setStatus(AssetStatus.APPROVED);

        Asset savedAsset = assetRepository.save(asset);
        return assetMapper.toDto(savedAsset);
    }

    @Override
    public AssetDetailResponseDto getAssetDetail(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + assetId));
        
        return assetMapper.toDetailDto(asset);
    }
}