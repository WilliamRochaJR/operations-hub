package com.operationshub.orders.order;

import com.operationshub.orders.outbox.OutboxRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "app.outbox.interval-ms=3600000")
class OrderPersistenceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.6-alpine");

    @Autowired OrderService service;
    @Autowired OrderRepository orders;
    @Autowired OutboxRepository outbox;

    @Test
    void persistsOrderAndOutboxEventInTheSameUseCase() {
        var response = service.create(
            new CreateOrderRequest("Integration Test", new BigDecimal("149.90"), "BRL"),
            "integration-correlation"
        );

        var persistedOrder = orders.findById(response.id()).orElseThrow();
        var pendingEvents = outbox.findTop20ByPublishedAtIsNullOrderByOccurredAtAsc();

        assertThat(persistedOrder.getCustomerName()).isEqualTo("Integration Test");
        assertThat(persistedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(pendingEvents)
            .singleElement()
            .satisfies(event -> {
                assertThat(event.getAggregateId()).isEqualTo(response.id());
                assertThat(event.getPayload()).contains("OrderCreated", "integration-correlation");
            });
    }
}
