package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.dto.ApproveAssetRequestDto;
import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.dto.AssetDetailResponseDto;
import com.horolofi.rwa.dto.RejectAssetRequestDto;
import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.entity.AssetStatus;
import com.horolofi.rwa.exception.AssetNotFoundException;
import com.horolofi.rwa.mapper.AssetMapper;
import com.horolofi.rwa.repository.AssetRepository;
import com.horolofi.rwa.service.TokenizationService;
import com.horolofi.rwa.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenizationServiceImpl implements TokenizationService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final FileStorageService fileStorageService; // Tambahkan ini

    @Override
    @Transactional
    public TokenizationResponseDto requestTokenization(TokenizationRequestDto request) {
        // Tambahkan ini untuk handle upload
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = fileStorageService.saveFile(request.getImage());
            if (request.getImageUrls() == null) {
                request.setImageUrls(new ArrayList<>());
            }
            request.getImageUrls().add(imageUrl);
        }

        // Map DTO to Entity
        Asset asset = assetMapper.toEntity(request);
        asset.setStatus(AssetStatus.PENDING);

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
        asset.setApprovedAt(LocalDateTime.now());

        Asset savedAsset = assetRepository.save(asset);
        return assetMapper.toDto(savedAsset);
    }

    @Override
    public AssetDetailResponseDto getAssetDetail(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + assetId));
        
        return assetMapper.toDetailDto(asset);
    }

    @Override
    @Transactional
    public TokenizationResponseDto rejectAsset(Long assetId, RejectAssetRequestDto request) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + assetId));

        // Validasi status jika diperlukan (misal: hanya yang PENDING yang bisa di-reject)
        if (asset.getStatus() != AssetStatus.PENDING) {
             throw new IllegalStateException("Only PENDING assets can be rejected");
        }
        
        asset.setAuditorNotes(request.getRejectionReason());   
        asset.setStatus(AssetStatus.REJECTED);
        asset.setRejectedAt(LocalDateTime.now());
        
        // Jika di Entity Asset ada field untuk menyimpan alasan reject, set di sini
        // asset.setRejectionReason(request.getRejectionReason());
        
        log.info("Asset {} rejected. Reason: {}", assetId, request.getRejectionReason());

        Asset savedAsset = assetRepository.save(asset);
        
        // Kirim email notifikasi reject jika diperlukan (opsional)
        // emailService.sendRejectionEmail(savedAsset.getUser().getEmail(), request.getRejectionReason());

        return assetMapper.toDto(savedAsset);
    }
}