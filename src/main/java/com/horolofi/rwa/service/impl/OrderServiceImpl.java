package com.horolofi.rwa.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horolofi.rwa.dto.CreateOrderRequest;
import com.horolofi.rwa.dto.CreateOrderResponse;
import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.entity.PriceHistory;
import com.horolofi.rwa.entity.OrderBook;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;
import com.horolofi.rwa.entity.User;
import com.horolofi.rwa.service.OrderService;
import com.horolofi.rwa.repository.OrderBookRepository;
import com.horolofi.rwa.repository.AssetRepository;
import com.horolofi.rwa.repository.PriceRepository;
import com.horolofi.rwa.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderBookRepository orderBookRepository;
    private final PriceRepository priceRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        
        // Ambil User (relasi)
        User buyer = userRepository.findFirstByWalletAddress(request.getWalletAddress()).orElse(null);
        
        // Ambil Asset (relasi)
        Asset asset = assetRepository.findById(request.getAssetId()).orElse(null);

        // Ambil harga terbaru jika ada, jika tidak set default 0
        PriceHistory latestPrice = priceRepository.findTopByProductIdOrderByRecordedAtDesc(request.getAssetId())
                .orElse(null);
        BigDecimal unitPrice = (latestPrice != null) ? latestPrice.getPrice() : BigDecimal.ZERO;
        
        // Hitung total price
        BigDecimal quantityBD = request.getQuantity();
        BigDecimal totalPrice = unitPrice.multiply(quantityBD);

        // Hitung Fee jika ada (misal 0.5%)
        BigDecimal feePercentage = new BigDecimal("0.005"); // 0.5%
        BigDecimal fee = totalPrice.multiply(feePercentage).setScale(2, RoundingMode.HALF_UP);

        // Build OrderBook
        OrderBook orderBook = new OrderBook();
        orderBook.setBuyer(buyer); // Set relasi User
        orderBook.setAsset(asset); // Set relasi Asset
        orderBook.setOrderType(OrderType.valueOf(request.getOrderType())); // Convert String to OrderType enum
        orderBook.setQuantity(quantityBD.intValue());
        orderBook.setPrice(totalPrice.doubleValue());
        orderBook.setFee(fee.doubleValue());
        orderBook.setTotalPrice(totalPrice.add(fee).doubleValue());
        orderBook.setStatus(OrderStatus.PENDING.name());
        orderBook.setCreatedAt(LocalDateTime.now());

        // Simpan
        OrderBook savedOrder = orderBookRepository.save(orderBook);

        // Return response
        return CreateOrderResponse.builder()
                .orderId(savedOrder.getId())
                .walletAddress(buyer.getWalletAddress())
                .orderType(savedOrder.getOrderType().toString())
                .assetId(asset.getId())
                .assetName(asset.getBrand())
                .quantity(savedOrder.getQuantity())
                .price(BigDecimal.valueOf(savedOrder.getPrice()))
                .fee(savedOrder.getFee())
                .totalPrice(savedOrder.getTotalPrice())
                .status(OrderStatus.valueOf(savedOrder.getStatus()).toString())
                .createdAt(savedOrder.getCreatedAt())
                .message("Order created successfully")
                .build();
    }
}