package com.operationshub.audit.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AuditRepository extends JpaRepository<AuditEntry, UUID> {
    List<AuditEntry> findByOrderIdOrderByOccurredAtAsc(UUID orderId);
}
