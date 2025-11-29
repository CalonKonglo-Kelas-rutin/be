package com.horolofi.rwa.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horolofi.rwa.dto.CreateOrderRequest;
import com.horolofi.rwa.dto.CreateOrderResponse;
import com.horolofi.rwa.dto.OrderBookItemDto;
import com.horolofi.rwa.dto.OrderBookListResponse;
import com.horolofi.rwa.dto.CancelOrderRequest; // Import DTO
import com.horolofi.rwa.dto.MatchOrderResponse;
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
import java.util.Optional;
import lombok.extern.slf4j.Slf4j; // Tambahkan import ini

@Service
@RequiredArgsConstructor
@Slf4j // Tambahkan anotasi ini untuk mengaktifkan 'log'
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
        orderBook.setMaker_address(maker); 
        orderBook.setAsset(asset); 
        orderBook.setOrderType(OrderType.valueOf(request.getOrderType()));
        orderBook.setQuantity(quantityBD.intValue());
        orderBook.setPrice(totalPrice.doubleValue());
        orderBook.setFee(fee.doubleValue());
        orderBook.setTotalPrice(totalPrice.add(fee).doubleValue());
        
        // --- UPDATE: Mapping sesuai kolom database ---
        orderBook.setMaker_signature_data(request.getSignatureData());
        orderBook.setMaker_expiry(request.getExpiryData());
        orderBook.setNonce(request.getNonce()); 
        // --------------------------------------------

        orderBook.setStatus((request.getOrderType().equalsIgnoreCase("BUY")) ? OrderStatus.OPEN : OrderStatus.ASK);
        orderBook.setCreatedAt(LocalDateTime.now());

        OrderBook savedOrder = orderBookRepository.save(orderBook);

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

    @Override
    public MatchOrderResponse matchOrder(CreateOrderRequest request) {
        log.info("--- START MATCHING PROCESS ---");
        log.info("Incoming Request from: {}", request.getWalletAddress());
        log.info("Looking to: {} Asset ID: {}", request.getOrderType(), request.getAssetId());

        // Tentukan tipe order yang dicari (Lawan jenis)
        OrderType requestType = OrderType.valueOf(request.getOrderType());
        OrderType lookingForType = requestType == OrderType.BUY ? OrderType.SELL : OrderType.BUY;
        OrderStatus lookingForStatus = requestType == OrderType.BUY ? OrderStatus.ASK : OrderStatus.OPEN;
        Long requestAssetId = request.getAssetId();

        log.info("System is searching for Order Type: {}", lookingForType);

        // Cari order di database yang cocok
        Optional<OrderBook> match = orderBookRepository.findAll().stream()
            .filter(o -> o.getStatus() == lookingForStatus
        )
            .filter(o -> o.getOrderType() == lookingForType)
            .filter(o -> {
                if (o.getAsset() == null) return false;
                return o.getAsset().getId().longValue() == requestAssetId.longValue();
            })
            .findFirst();

        if (match.isPresent()) {
            OrderBook makerOrder = match.get();
            
            makerOrder.setStatus(OrderStatus.MATCHED);

            List<User> takerUsers = userRepository.findByWalletAddress(request.getWalletAddress());
            if (!takerUsers.isEmpty()) {
                makerOrder.setTaker_address(takerUsers.get(0));
            }
            
            makerOrder.setUpdatedAt(LocalDateTime.now()); 
            orderBookRepository.save(makerOrder);

            return MatchOrderResponse.builder()
                .status("MATCH_FOUND")
                .match_data(MatchOrderResponse.MatchData.builder()
                    .order_id(makerOrder.getId())
                    .maker_address(makerOrder.getMaker_address().getWalletAddress())
                    .quantity(String.valueOf(makerOrder.getQuantity()))
                    .price(makerOrder.getPrice().toString())
                    .fee(makerOrder.getFee() != null ? makerOrder.getFee().toString() : "0")
                    .totalPrice(makerOrder.getTotalPrice() != null ? makerOrder.getTotalPrice().toString() : "0")
                    
                    // --- UPDATE: Return field sesuai kolom database ---
                    .maker_signature_data(makerOrder.getMaker_signature_data())
                    .maker_expiry(makerOrder.getMaker_expiry())
                    .maker_nonce(makerOrder.getNonce())
                    .build())
                .build();
        } else {
             // Tambahkan log detail kenapa gagal
             log.warn("NO MATCH FOUND. Debug Info:");
             log.warn("Looking for Type: {}", lookingForType);
             log.warn("Looking for Asset ID: {}", requestAssetId); // Sekarang variabel ini sudah dikenali
             log.warn("Total Orders in DB: {}", orderBookRepository.count());
             
             return MatchOrderResponse.builder()
                .status("NO_MATCH_FOUND")
                .match_data(null)
                .build();
        }
    }

    @Override
    public OrderBookListResponse getOrdersByUserAndAsset(String walletAddress, String assetId, OrderStatus status, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<OrderBook> orderPage;
        
        // Konversi assetId String ke Long agar sesuai dengan tipe ID di database
        Long assetIdLong = Long.parseLong(assetId);

        if (status != null) {
            orderPage = orderBookRepository.findByUserWalletAddressAndAssetIdAndStatus(walletAddress, assetIdLong, status, pageable);
        } else {
            orderPage = orderBookRepository.findByUserWalletAddressAndAssetId(walletAddress, assetIdLong, pageable);
        }

        // Mapping Entity ke DTO
        List<OrderBookItemDto> items = orderPage.getContent().stream()
                .map(order -> OrderBookItemDto.builder()
                        .orderId(order.getId())
                        .userAddress(order.getMaker_address().getWalletAddress())
                        .quantity(order.getQuantity())    
                        .price(BigDecimal.valueOf(order.getPrice()))            
                        .orderType(order.getOrderType())    
                        .status(order.getStatus())          
                        .createdAt(order.getCreatedAt())    
                        .build())   
                .toList();

        return OrderBookListResponse.builder()
                .status("success")
                .meta(OrderBookListResponse.Meta.builder()
                        .totalQueue(orderPage.getTotalElements())
                        .assetId(assetId)
                        .build())
                .data(items)
                .build();
    }
}