package com.operationshub.orders.outbox;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id private UUID id;
    @Column(nullable = false) private UUID aggregateId;
    @Column(nullable = false) private String eventType;
    @Column(nullable = false, columnDefinition = "text") private String payload;
    @Column(nullable = false) private Instant occurredAt;
    @Column(nullable = false) private String correlationId;
    private Instant publishedAt;

    protected OutboxEvent() {}
    public OutboxEvent(UUID id, UUID aggregateId, String eventType, String payload, Instant occurredAt, String correlationId) {
        this.id = id; this.aggregateId = aggregateId; this.eventType = eventType; this.payload = payload;
        this.occurredAt = occurredAt; this.correlationId = correlationId;
    }
    public UUID getId() { return id; }
    public UUID getAggregateId() { return aggregateId; }
    public String getPayload() { return payload; }
    public void markPublished(Instant instant) { this.publishedAt = instant; }
}
