package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.CreateOrderRequest;
import com.horolofi.rwa.dto.CreateOrderResponse;
import com.horolofi.rwa.dto.OrderBookListResponse;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);

    OrderBookListResponse getOrderBook(String assetId, OrderType side, OrderStatus status, int page, int limit);
}