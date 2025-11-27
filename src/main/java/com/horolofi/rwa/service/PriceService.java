package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.PriceHistoryDto;
import java.util.List;

public interface PriceService {
    List<PriceHistoryDto> getChartData(Long productId, String range);
}