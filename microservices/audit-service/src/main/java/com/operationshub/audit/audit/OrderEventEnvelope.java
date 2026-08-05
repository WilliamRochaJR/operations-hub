package com.operationshub.audit.audit;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record OrderEventEnvelope(UUID eventId, String eventType, int schemaVersion, Instant occurredAt, String correlationId, UUID aggregateId, Map<String, Object> payload) {}
