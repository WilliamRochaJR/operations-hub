package com.operationshub.audit.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
class AuditPersistenceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.6-alpine");

    @Autowired OrderEventConsumer consumer;
    @Autowired AuditRepository repository;

    @Test
    void appliesFlywayMigrationAndPersistsEventIdempotently() throws Exception {
        var eventId = UUID.randomUUID();
        var orderId = UUID.randomUUID();
        var message = """
            {
              "eventId":"%s",
              "eventType":"OrderCreated",
              "schemaVersion":1,
              "occurredAt":"%s",
              "correlationId":"integration-correlation",
              "aggregateId":"%s",
              "payload":{"orderNumber":"ORD-TEST","status":"PENDING"}
            }
            """.formatted(eventId, Instant.now(), orderId);

        consumer.consume(message);
        consumer.consume(message);

        assertThat(repository.findByOrderIdOrderByOccurredAtAsc(orderId))
            .singleElement()
            .satisfies(entry -> {
                assertThat(entry.getEventId()).isEqualTo(eventId);
                assertThat(entry.getCorrelationId()).isEqualTo("integration-correlation");
            });
    }
}
