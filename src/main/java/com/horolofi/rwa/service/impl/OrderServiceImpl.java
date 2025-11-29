package com.horolofi.rwa.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horolofi.rwa.dto.CreateOrderRequest;
import com.horolofi.rwa.dto.CreateOrderResponse;
import com.horolofi.rwa.dto.OrderBookItemDto;
import com.horolofi.rwa.dto.OrderBookListResponse;
import com.horolofi.rwa.dto.CancelOrderRequest; // Import DTO
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;

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
        
        // Ambil User (relasi) - Maker
        User maker = userRepository.findFirstByWalletAddress(request.getWalletAddress()).orElse(null);
        
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
        orderBook.setMaker_address(maker); // UBAH INI: Ptass object User, bukan String
        orderBook.setAsset(asset); // Set relasi Asset
        orderBook.setOrderType(OrderType.valueOf(request.getOrderType()));// Convert String to OrderType enum
        orderBook.setQuantity(quantityBD.intValue());
        orderBook.setPrice(totalPrice.doubleValue());
        orderBook.setFee(fee.doubleValue());
        orderBook.setTotalPrice(totalPrice.add(fee).doubleValue());
        orderBook.setSignatureData(request.getSignatureData());
        orderBook.setStatus((request.getOrderType().equalsIgnoreCase("BUY")) ? OrderStatus.OPEN : OrderStatus.ASK);
        orderBook.setCreatedAt(LocalDateTime.now());

        // Simpan
        OrderBook savedOrder = orderBookRepository.save(orderBook);

        // Return response
        return CreateOrderResponse.builder()
                .orderId(savedOrder.getId())
                .walletAddress(maker.getWalletAddress())
                .orderType(savedOrder.getOrderType().toString())
                .assetId(asset.getId())
                .assetName(asset.getBrand())
                .quantity(savedOrder.getQuantity())
                .price(BigDecimal.valueOf(savedOrder.getPrice()))
                .fee(savedOrder.getFee())
                .totalPrice(savedOrder.getTotalPrice())
                .status(savedOrder.getStatus().toString())
                .createdAt(savedOrder.getCreatedAt())
                .message("Order created successfully")
                .build();
    }

    @Override
    public OrderBookListResponse getOrderBook(String assetId, OrderType side, OrderStatus status, int page, int limit) {
        // Default status to OPEN if not provided
        OrderStatus queryStatus = (status != null) ? status : OrderStatus.OPEN;
        
        // Sort by createdAt ASC (FIFO)
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").ascending());
        
        Page<OrderBook> orderPage = orderBookRepository.findByAssetIdAndOrderTypeAndStatus(assetId, side, queryStatus, pageable);
        long totalQueue = orderBookRepository.countByAssetIdAndOrderTypeAndStatus(assetId, side, queryStatus);
        
        List<OrderBookItemDto> items = new ArrayList<>();
        long startPosition = (long) page * limit + 1;
        
        for (int i = 0; i < orderPage.getContent().size(); i++) {
            OrderBook order = orderPage.getContent().get(i);
            items.add(OrderBookItemDto.builder()
                    .orderId(order.getId())
                    .userAddress(order.getMaker_address().getWalletAddress()) 
                    .quantity(order.getQuantity())
                    .createdAt(order.getCreatedAt())
                    .queuePosition(startPosition + i)
                    .build());
        }
        
        return OrderBookListResponse.builder()
                .status("success")
                .meta(OrderBookListResponse.Meta.builder()
                        .totalQueue(totalQueue)
                        .assetId(assetId)
                        .build())
                .data(items)
                .build();
    }

    @Override
    @Transactional
    public CreateOrderResponse cancelOrder(CancelOrderRequest request) {
        // 1. Cari Order berdasarkan ID
        OrderBook order = orderBookRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

        // 2. Validasi Pemilik (Wallet Address harus sama dengan Maker)
        if (!order.getMaker_address().getWalletAddress().equalsIgnoreCase(request.getWalletAddress())) {
            throw new RuntimeException("Unauthorized: Wallet address does not match order owner");
        }

        // 3. Validasi Status (Hanya bisa cancel jika status OPEN atau ASK)
        if (order.getStatus() == OrderStatus.MATCHED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }

        // 4. Update Status
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        OrderBook savedOrder = orderBookRepository.save(order);

        // 5. Return response (Reuse CreateOrderResponse)
        return CreateOrderResponse.builder()
                .orderId(savedOrder.getId())
                .walletAddress(savedOrder.getMaker_address().getWalletAddress())
                .orderType(savedOrder.getOrderType().toString())
                .assetId(savedOrder.getAsset().getId())
                .assetName(savedOrder.getAsset().getBrand())
                .quantity(savedOrder.getQuantity())
                .price(BigDecimal.valueOf(savedOrder.getPrice()))
                .fee(savedOrder.getFee())
                .totalPrice(savedOrder.getTotalPrice())
                .status(savedOrder.getStatus().toString())
                .createdAt(savedOrder.getCreatedAt())
                .message("Order cancelled successfully")
                .build();
    }
}