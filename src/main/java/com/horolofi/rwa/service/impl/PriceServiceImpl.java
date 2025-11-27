package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.mapper.PriceMapper;
import com.horolofi.rwa.dto.PriceHistoryDto;
import com.horolofi.rwa.entity.PriceHistory;
import com.horolofi.rwa.repository.PriceRepository;
import com.horolofi.rwa.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository repository;
    private final PriceMapper priceMapper;

    @Override
    public List<PriceHistoryDto> getChartData(Long productId) {
        // Abaikan parameter 'range', langsung ambil semua data
        List<PriceHistory> entities = repository.findByProductIdOrderByRecordedAtAsc(productId);
        
        return priceMapper.toDtoList(entities);
    }
}