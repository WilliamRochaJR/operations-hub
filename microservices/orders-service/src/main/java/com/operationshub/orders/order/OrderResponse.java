package com.operationshub.orders.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(UUID id, String number, String customerName, BigDecimal totalAmount, String currency, OrderStatus status, Instant createdAt) {
    static OrderResponse from(OrderEntity order) {
        return new OrderResponse(order.getId(), order.getNumber(), order.getCustomerName(), order.getTotalAmount(), order.getCurrency(), order.getStatus(), order.getCreatedAt());
    }
}
