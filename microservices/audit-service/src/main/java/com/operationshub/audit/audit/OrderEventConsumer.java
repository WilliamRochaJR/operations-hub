package com.operationshub.audit.audit;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderEventConsumer {
    private final AuditRepository repository;
    private final ObjectMapper objectMapper;

    OrderEventConsumer(AuditRepository repository, ObjectMapper objectMapper) {
        this.repository = repository; this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.kafka.orders-topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void consume(String message) throws JacksonException {
        var event = objectMapper.readValue(message, OrderEventEnvelope.class);
        if (repository.existsById(event.eventId())) return;
        repository.save(new AuditEntry(event.eventId(), event.aggregateId(), event.eventType(), event.occurredAt(), event.correlationId(), message));
    }
}
