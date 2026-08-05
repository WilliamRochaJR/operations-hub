import { http } from '../../../services/http/http-client';
import type { AuditEntry, Order } from '../types/order';

export interface CreateOrderInput {
  customerName: string;
  totalAmount: number;
  currency: string;
}

export const ordersService = {
  create: (input: CreateOrderInput) =>
    http<Order>('/orders', { method: 'POST', body: JSON.stringify(input) }),
  audit: (orderId: string) => http<AuditEntry[]>(`/orders/${orderId}/audit`),
};
