package com.horolofi.rwa.mapper;

import com.horolofi.rwa.dto.PriceHistoryDto;
import com.horolofi.rwa.entity.PriceHistory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceMapper {

    // Formatter untuk mengubah LocalDateTime database ke String yang cantik
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PriceHistoryDto toDto(PriceHistory entity) {
        if (entity == null) {
            return null;
        }

        return PriceHistoryDto.builder()
                .date(entity.getRecordedAt() != null ? entity.getRecordedAt().format(FORMATTER) : null)
                .price(entity.getPrice())
                .minPrice(entity.getMinPrice())
                .maxPrice(entity.getMaxPrice())
                .currency(entity.getCurrency())
                .build();
    }

    public List<PriceHistoryDto> toDtoList(List<PriceHistory> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    // Jika nanti butuh toEntity (untuk create data baru), tinggal tambahkan di sini
}