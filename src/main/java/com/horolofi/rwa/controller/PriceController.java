package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.PriceHistoryDto;
import com.horolofi.rwa.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService; // Panggil Interface, bukan Impl!

    @GetMapping("/history")
    public ResponseEntity<List<PriceHistoryDto>> getChart(
            @RequestParam Long productId
    ) {
        // Controller cuma terima request -> lempar ke service -> balikin response
        List<PriceHistoryDto> data = priceService.getChartData(productId);
        return ResponseEntity.ok(data);
    }
}