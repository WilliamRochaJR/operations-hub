import { HttpService } from '@nestjs/axios';
import { Injectable } from '@nestjs/common';
import { randomUUID } from 'node:crypto';
import { firstValueFrom } from 'rxjs';
import type { CreateOrderDto } from './dto/create-order.dto';

@Injectable()
export class OrdersService {
  private readonly ordersUrl = process.env.ORDERS_SERVICE_URL ?? 'http://localhost:8081';
  private readonly auditUrl = process.env.AUDIT_SERVICE_URL ?? 'http://localhost:8082';

  constructor(private readonly http: HttpService) {}

  async create(input: CreateOrderDto, correlationId?: string) {
    const response = await firstValueFrom(this.http.post(`${this.ordersUrl}/orders`, input, {
      headers: { 'x-correlation-id': correlationId ?? randomUUID() },
    }));
    return response.data;
  }

  async audit(orderId: string, correlationId?: string) {
    const response = await firstValueFrom(this.http.get(`${this.auditUrl}/audit/orders/${orderId}`, {
      headers: { 'x-correlation-id': correlationId ?? randomUUID() },
    }));
    return response.data;
  }
}
