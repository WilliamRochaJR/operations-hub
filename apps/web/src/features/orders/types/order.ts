export type OrderStatus = 'PENDING' | 'APPROVED' | 'PROCESSING' | 'SHIPPED' | 'CANCELLED';

export interface Order {
  id: string;
  number: string;
  customerName: string;
  totalAmount: number;
  currency: string;
  status: OrderStatus;
  createdAt: string;
}

export interface AuditEntry {
  eventId: string;
  orderId: string;
  eventType: string;
  occurredAt: string;
}
