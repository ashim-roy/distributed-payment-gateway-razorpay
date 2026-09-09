package com.AshimCS.razorpay.payment.service.impl;

import com.AshimCS.razorpay.common.enums.OrderStatus;
import com.AshimCS.razorpay.common.exception.DuplicateResourceException;
import com.AshimCS.razorpay.payment.dto.request.CreateOrderRequest;
import com.AshimCS.razorpay.payment.dto.response.OrderResponse;
import com.AshimCS.razorpay.payment.entity.OrderRecord;
import com.AshimCS.razorpay.payment.entity.Payment;
import com.AshimCS.razorpay.payment.repository.OrderRepository;
import com.AshimCS.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;// Default expiry time in minutes

    @Override
    public OrderResponse createOrder(UUID merchantId, CreateOrderRequest request) {

        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())) {
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists: " + request.receipt());
        }

        // If not null and is not a duplicate Now we create a new record."
        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes)) // Set expiresAt if provided,
                .build();

        order = orderRepository.save(order);

        return new OrderResponse(order.getId(),
                order.getMerchantId(), order.getReceipt(),
                order.getAmount(), order.getOrderStatus(),
                order.getAttempts(), order.getNotes(),
                order.getExpiresAt(), null); // Payment is null at this point since the order is just created

        return null;
    }
}
