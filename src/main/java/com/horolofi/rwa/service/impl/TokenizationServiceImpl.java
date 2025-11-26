package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.mapper.AssetMapper;
import com.horolofi.rwa.repository.AssetRepository;
import com.horolofi.rwa.service.TokenizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 1. Tambahkan import ini
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // 2. Tambahkan anotasi ini agar variabel 'log' dikenali
public class TokenizationServiceImpl implements TokenizationService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;

    @Override
    @Transactional
    public TokenizationResponseDto requestTokenization(TokenizationRequestDto request) {
        log.info("Processing tokenization request for owner: {}, brand: {}, model: {}", 
                request.getOwnerId(), request.getBrand(), request.getModel());

        // Map DTO to Entity
        Asset asset = assetMapper.toEntity(request);

        // Save to database
        Asset savedAsset = assetRepository.save(asset);

        log.info("Asset created successfully with ID: {} and status: {}", 
                savedAsset.getId(), savedAsset.getStatus());

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
}