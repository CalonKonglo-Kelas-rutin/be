package com.horolofi.rwa.controller;

import com.horolofi.rwa.dto.AssetListingDto;
import com.horolofi.rwa.dto.CreateOrderRequest;
import com.horolofi.rwa.dto.CreateOrderResponse;
import com.horolofi.rwa.dto.OrderBookListResponse;
import com.horolofi.rwa.dto.CancelOrderRequest; // Import DTO
import com.horolofi.rwa.dto.MatchOrderResponse;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;
import com.horolofi.rwa.entity.AssetStatus; 
import com.horolofi.rwa.service.AssetService;
import com.horolofi.rwa.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MarketController {
    
    private final OrderService orderService;
    private final AssetService assetService;
    
    @PostMapping("/order/create")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("Received create order request: {}", request);
        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/listings")
    public ResponseEntity<List<AssetListingDto>> getListings(
        @RequestParam(required = false, defaultValue = "APPROVED") AssetStatus assetStatus
    ) {
        log.info("Fetching tokenized assets for listings");
        log.info("Fetching tokenized assets for listings, assetStatus={}", assetStatus);
        List<AssetListingDto> listings = assetService.getTokenizedAssets(assetStatus);
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderBookListResponse> getOrderBook(
            @RequestParam String assetId,
            @RequestParam OrderType side,
            @RequestParam(required = false, defaultValue = "OPEN") OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("Fetching order book for asset: {}, side: {}, status: {}", assetId, side, status);
        OrderBookListResponse response = orderService.getOrderBook(assetId, side, status, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/user")
    public ResponseEntity<OrderBookListResponse> getUserOrders(
            @RequestParam String walletAddress,
            @RequestParam String assetId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("Fetching orders for wallet: {}, asset: {}, status: {}", walletAddress, assetId, status);
        OrderBookListResponse response = orderService.getOrdersByUserAndAsset(walletAddress, assetId, status, page, limit);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/order/cancel")
    public ResponseEntity<CreateOrderResponse> cancelOrder(@Valid @RequestBody CancelOrderRequest request) {
        log.info("Received cancel order request: {}", request);
        CreateOrderResponse response = orderService.cancelOrder(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/orders/match")
    public ResponseEntity<MatchOrderResponse> matchOrder(@RequestBody CreateOrderRequest request) {
        MatchOrderResponse response = orderService.matchOrder(request);
        return ResponseEntity.ok(response);
    }
}