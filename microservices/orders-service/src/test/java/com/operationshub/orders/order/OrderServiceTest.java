package com.operationshub.orders.order;

import com.operationshub.orders.outbox.OutboxRepository;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Test
    void storesOrderAndOutboxInTheSameUseCase() throws Exception {
        var orders = mock(OrderRepository.class);
        var outbox = mock(OutboxRepository.class);
        var json = mock(ObjectMapper.class);
        when(orders.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(json.writeValueAsString(any())).thenReturn("{}");
        var service = new OrderService(orders, outbox, json);

        var result = service.create(new CreateOrderRequest("Cliente", new BigDecimal("149.90"), "BRL"), "correlation-1");

        assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.number()).startsWith("ORD-");
        verify(orders).save(any(OrderEntity.class));
        verify(outbox).save(any());
    }
}
