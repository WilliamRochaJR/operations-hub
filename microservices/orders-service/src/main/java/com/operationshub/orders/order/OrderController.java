package com.operationshub.orders.order;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;
    OrderController(OrderService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponse create(@Valid @RequestBody CreateOrderRequest request, @RequestHeader(value = "x-correlation-id", required = false) String correlationId) {
        return service.create(request, correlationId == null ? UUID.randomUUID().toString() : correlationId);
    }
}
