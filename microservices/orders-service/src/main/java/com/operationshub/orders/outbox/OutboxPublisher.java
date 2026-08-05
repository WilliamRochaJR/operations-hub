package com.operationshub.orders.outbox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Component
public class OutboxPublisher {
    private final OutboxRepository repository;
    private final KafkaTemplate<String, String> kafka;
    private final String topic;

    OutboxPublisher(OutboxRepository repository, KafkaTemplate<String, String> kafka, @Value("${app.kafka.orders-topic}") String topic) {
        this.repository = repository; this.kafka = kafka; this.topic = topic;
    }

    @Scheduled(fixedDelayString = "${app.outbox.interval-ms:500}")
    @Transactional
    public void publish() {
        for (var event : repository.findTop20ByPublishedAtIsNullOrderByOccurredAtAsc()) {
            kafka.send(topic, event.getAggregateId().toString(), event.getPayload()).join();
            event.markPublished(Instant.now());
        }
    }
}
