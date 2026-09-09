package com.AshimCS.razorpay.payment.controller;

import com.AshimCS.razorpay.payment.dto.request.CreateOrderRequest;
import com.AshimCS.razorpay.payment.dto.response.OrderResponse;
import com.AshimCS.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    // using  a dummy merchantId for now, in future we can get it from the authenticated user context
    UUID merchantId = UUID.fromString("28852a2e-011d-4dea-bcb1-4d3faf2ef795"); // TODO, replace with merchant context using spring security. its global. thread scoped available for this request

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        // Implementation for creating an order
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(merchantId, request));

    }

}
