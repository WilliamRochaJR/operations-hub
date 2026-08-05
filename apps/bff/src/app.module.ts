import { Module } from '@nestjs/common';
import { HttpModule } from '@nestjs/axios';
import { HealthController } from './health/health.controller';
import { OrdersController } from './orders/orders.controller';
import { OrdersService } from './orders/orders.service';

@Module({
  imports: [HttpModule.register({ timeout: 5_000 })],
  controllers: [HealthController, OrdersController],
  providers: [OrdersService],
})
export class AppModule {}
