package com.operationshub.audit.audit;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_entries")
public class AuditEntry {
    @Id private UUID eventId;
    @Column(nullable = false) private UUID orderId;
    @Column(nullable = false) private String eventType;
    @Column(nullable = false) private Instant occurredAt;
    @Column(nullable = false) private String correlationId;
    @Column(nullable = false, columnDefinition = "text") private String payload;

    protected AuditEntry() {}
    public AuditEntry(UUID eventId, UUID orderId, String eventType, Instant occurredAt, String correlationId, String payload) {
        this.eventId = eventId; this.orderId = orderId; this.eventType = eventType; this.occurredAt = occurredAt;
        this.correlationId = correlationId; this.payload = payload;
    }
    public UUID getEventId() { return eventId; }
    public UUID getOrderId() { return orderId; }
    public String getEventType() { return eventType; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getCorrelationId() { return correlationId; }
}
