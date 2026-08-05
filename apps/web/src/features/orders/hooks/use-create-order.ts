import { useMutation, useQuery } from '@tanstack/react-query';
import { ordersService, type CreateOrderInput } from '../services/orders.service';

export function useCreateOrder() {
  return useMutation({ mutationFn: (input: CreateOrderInput) => ordersService.create(input) });
}

export function useOrderAudit(orderId?: string) {
  return useQuery({
    queryKey: ['order-audit', orderId],
    queryFn: () => ordersService.audit(orderId!),
    enabled: Boolean(orderId),
    refetchInterval: (query) => (query.state.data?.length ? false : 1_000),
  });
}
