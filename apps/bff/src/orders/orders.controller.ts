import { Body, Controller, Get, Headers, Param, Post } from '@nestjs/common';
import { CreateOrderDto } from './dto/create-order.dto';
import { OrdersService } from './orders.service';

@Controller('orders')
export class OrdersController {
  constructor(private readonly orders: OrdersService) {}

  @Post()
  create(@Body() input: CreateOrderDto, @Headers('x-correlation-id') correlationId?: string) {
    return this.orders.create(input, correlationId);
  }

  @Get(':id/audit')
  audit(@Param('id') id: string, @Headers('x-correlation-id') correlationId?: string) {
    return this.orders.audit(id, correlationId);
  }
}
