package com.operationshub.audit.audit;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import static org.mockito.Mockito.*;

class OrderEventConsumerTest {
    @Test
    void ignoresAnEventThatWasAlreadyProcessed() throws Exception {
        var repository = mock(AuditRepository.class);
        var json = mock(ObjectMapper.class);
        var eventId = UUID.randomUUID();
        var envelope = new OrderEventEnvelope(eventId, "OrderCreated", 1, Instant.now(), "correlation-1", UUID.randomUUID(), Map.of());
        when(json.readValue("{}", OrderEventEnvelope.class)).thenReturn(envelope);
        when(repository.existsById(eventId)).thenReturn(true);

        new OrderEventConsumer(repository, json).consume("{}");

        verify(repository, never()).save(any());
    }
}
