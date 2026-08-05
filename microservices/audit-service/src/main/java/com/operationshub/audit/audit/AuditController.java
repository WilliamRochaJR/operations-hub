package com.operationshub.audit.audit;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/audit")
public class AuditController {
    private final AuditRepository repository;
    AuditController(AuditRepository repository) { this.repository = repository; }

    @GetMapping("/orders/{orderId}")
    List<AuditResponse> byOrder(@PathVariable UUID orderId) {
        return repository.findByOrderIdOrderByOccurredAtAsc(orderId).stream().map(AuditResponse::from).toList();
    }
}
