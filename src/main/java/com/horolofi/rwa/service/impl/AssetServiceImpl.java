// filepath: d:\Pribadi\Belajar-2025\Kelas-Rutin\CalonKonglo\be\src\main\java\com\horolofi\rwa\service\impl\AssetServiceImpl.java
package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.dto.AssetListingDto;
import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.entity.AssetStatus;
import com.horolofi.rwa.entity.PriceHistory;
import com.horolofi.rwa.repository.AssetRepository;
import com.horolofi.rwa.repository.PriceRepository;
import com.horolofi.rwa.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final PriceRepository priceRepository;

    @Override
    public List<AssetListingDto> getTokenizedAssets(AssetStatus status) {
        List<Asset> tokenizedAssets = (status == null)
                ? assetRepository.findAll()
                : assetRepository.findByStatus(status);
        return tokenizedAssets.stream()
                .map(asset -> {
                    Optional<PriceHistory> latestPriceOpt = priceRepository.findTopByProductIdOrderByRecordedAtDesc(asset.getId().longValue());
                    BigDecimal latestPrice = latestPriceOpt.map(PriceHistory::getPrice).orElse(BigDecimal.ZERO);
                    return new AssetListingDto(
                            asset.getId(),
                            asset.getStatus().name(),
                            asset.getTokenId(),
                            asset.getBrand(),
                            asset.getModel(),
                            asset.getImageUrls(),
                            latestPrice
                    );
                })
                .collect(Collectors.toList());
    }
}