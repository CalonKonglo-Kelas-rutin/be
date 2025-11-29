package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.CancelOrderRequest; // Import DTO baru
import com.horolofi.rwa.dto.CreateOrderRequest;
import com.horolofi.rwa.dto.CreateOrderResponse;
import com.horolofi.rwa.dto.MatchOrderResponse; // Import added
import com.horolofi.rwa.dto.OrderBookListResponse;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);
    MatchOrderResponse matchOrder(CreateOrderRequest request); // Method added
    OrderBookListResponse getOrderBook(String assetId, OrderType side, OrderStatus status, int page, int limit);
    CreateOrderResponse cancelOrder(CancelOrderRequest request);
}