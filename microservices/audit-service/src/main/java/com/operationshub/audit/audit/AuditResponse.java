package com.operationshub.audit.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditResponse(UUID eventId, UUID orderId, String eventType, Instant occurredAt, String correlationId) {
    static AuditResponse from(AuditEntry entry) {
        return new AuditResponse(entry.getEventId(), entry.getOrderId(), entry.getEventType(), entry.getOccurredAt(), entry.getCorrelationId());
    }
}
