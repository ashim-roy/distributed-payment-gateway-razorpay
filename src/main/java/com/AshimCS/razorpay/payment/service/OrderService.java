package com.AshimCS.razorpay.payment.service;

import com.AshimCS.razorpay.payment.dto.request.CreateOrderRequest;
import com.AshimCS.razorpay.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID merchantId, CreateOrderRequest request);
}
