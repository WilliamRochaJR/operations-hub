package com.operationshub.orders.order;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateOrderRequest(
    @NotBlank @Size(max = 120) String customerName,
    @NotNull @DecimalMin("0.01") BigDecimal totalAmount,
    @NotBlank @Pattern(regexp = "BRL") String currency
) {}
