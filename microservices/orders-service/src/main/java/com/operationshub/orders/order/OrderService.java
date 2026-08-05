package com.operationshub.orders.order;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.operationshub.orders.outbox.OutboxEvent;
import com.operationshub.orders.outbox.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final OutboxRepository outbox;
    private final ObjectMapper objectMapper;

    OrderService(OrderRepository orders, OutboxRepository outbox, ObjectMapper objectMapper) {
        this.orders = orders; this.outbox = outbox; this.objectMapper = objectMapper;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request, String correlationId) {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var order = orders.save(new OrderEntity(id, "ORD-" + id.toString().substring(0, 8).toUpperCase(), request.customerName(), request.totalAmount(), request.currency(), now));
        try {
            var envelope = Map.of(
                "eventId", UUID.randomUUID().toString(), "eventType", "OrderCreated", "schemaVersion", 1,
                "occurredAt", now.toString(), "correlationId", correlationId, "aggregateId", id.toString(),
                "payload", Map.of("orderNumber", order.getNumber(), "status", order.getStatus().name())
            );
            outbox.save(new OutboxEvent(UUID.fromString((String) envelope.get("eventId")), id, "OrderCreated", objectMapper.writeValueAsString(envelope), now, correlationId));
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize OrderCreated", exception);
        }
        return OrderResponse.from(order);
    }
}
