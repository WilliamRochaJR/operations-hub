package com.operationshub.orders.order;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id private UUID id;
    @Column(nullable = false, unique = true) private String number;
    @Column(nullable = false) private String customerName;
    @Column(nullable = false, precision = 15, scale = 2) private BigDecimal totalAmount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus status;
    @Column(nullable = false) private Instant createdAt;

    protected OrderEntity() {}

    public OrderEntity(UUID id, String number, String customerName, BigDecimal totalAmount, String currency, Instant createdAt) {
        this.id = id; this.number = number; this.customerName = customerName; this.totalAmount = totalAmount;
        this.currency = currency; this.status = OrderStatus.PENDING; this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getNumber() { return number; }
    public String getCustomerName() { return customerName; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
