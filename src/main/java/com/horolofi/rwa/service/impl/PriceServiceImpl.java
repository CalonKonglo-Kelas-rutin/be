package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.mapper.PriceMapper;
import com.horolofi.rwa.dto.PriceHistoryDto;
import com.horolofi.rwa.entity.PriceHistory;
import com.horolofi.rwa.repository.PriceRepository;
import com.horolofi.rwa.service.PriceService;
import com.horolofi.rwa.exception.AssetNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    @Override
    public List<PriceHistoryDto> getChartData(Long productId) {
        // Abaikan parameter 'range', langsung ambil semua data
        List<PriceHistory> entities = priceRepository.findByProductIdOrderByRecordedAtAsc(productId);
        
        return priceMapper.toDtoList(entities);
    }

    @Override
    public PriceHistoryDto getLatestPrice(Long productId) {
        // Gunakan method yang baru (parameter assetId dipassing ke productId)
        PriceHistory priceHistory = priceRepository.findTopByProductIdOrderByRecordedAtDesc(productId)
                .orElseThrow(() -> new AssetNotFoundException("Price history not found for asset: " + productId));
        
        return priceMapper.toDto(priceHistory);
    }
}