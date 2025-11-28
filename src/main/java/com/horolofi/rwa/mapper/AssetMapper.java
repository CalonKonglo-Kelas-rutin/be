package com.horolofi.rwa.mapper;

import com.horolofi.rwa.dto.AssetDetailResponseDto;
import com.horolofi.rwa.dto.TokenizationRequestDto;
import com.horolofi.rwa.dto.TokenizationResponseDto;
import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.entity.AssetStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetMapper {

    public Asset toEntity(TokenizationRequestDto dto) {
        return Asset.builder()
                .ownerId(dto.getOwnerId())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .refNumber(dto.getRefNumber())
                .serialNumber(dto.getSerialNumber())
                .productionYear(dto.getProductionYear())
                .conditionRating(dto.getConditionRating())
                .hasBox(dto.getHasBox())
                .hasPapers(dto.getHasPapers())
                .imageUrls(listToString(dto.getImageUrls()))
                .documentsUrl(listToString(dto.getDocumentsUrl()))
                .status(AssetStatus.PENDING)
                .build();
    }

    public TokenizationResponseDto toDto(Asset entity) {
        return TokenizationResponseDto.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .refNumber(entity.getRefNumber())
                .serialNumber(entity.getSerialNumber())
                .productionYear(entity.getProductionYear())
                .conditionRating(entity.getConditionRating())
                .hasBox(entity.getHasBox())
                .hasPapers(entity.getHasPapers())
                .imageUrls(entity.getImageUrls())
                .documentsUrl(entity.getDocumentsUrl())
                .status(entity.getStatus())
                .auditorNotes(entity.getAuditorNotes())
                .appraisedValueUsd(entity.getAppraisedValueUsd())
                .ipfsMetadataUri(entity.getIpfsMetadataUri())
                .tokenId(entity.getTokenId())
                .txHashMint(entity.getTxHashMint())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public AssetDetailResponseDto toDetailDto(Asset asset) {
        if (asset == null) {
            return null;
        }
        return AssetDetailResponseDto.builder()
                .id(asset.getId())
                .ownerId(asset.getOwnerId())
                .brand(asset.getBrand())
                .model(asset.getModel())
                .refNumber(asset.getRefNumber())
                .serialNumber(asset.getSerialNumber())
                .productionYear(asset.getProductionYear())
                .conditionRating(asset.getConditionRating())
                .hasBox(asset.getHasBox())
                .hasPapers(asset.getHasPapers())
                .imageUrls(asset.getImageUrls())
                .documentsUrl(asset.getDocumentsUrl())
                .status(asset.getStatus() != null ? asset.getStatus() : null)
                .auditorNotes(asset.getAuditorNotes())
                .appraisedValueUsd(asset.getAppraisedValueUsd())
                .ipfsMetadataUri(asset.getIpfsMetadataUri())
                .tokenId(asset.getTokenId())
                .txHashMint(asset.getTxHashMint())
                .createdAt(asset.getCreatedAt())
                .build();
    }

    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join(",", list);
    }
}