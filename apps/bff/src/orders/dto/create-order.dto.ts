import { IsIn, IsNumber, IsPositive, IsString, Length } from 'class-validator';

export class CreateOrderDto {
  @IsString()
  @Length(2, 120)
  customerName!: string;

  @IsNumber({ maxDecimalPlaces: 2 })
  @IsPositive()
  totalAmount!: number;

  @IsIn(['BRL'])
  currency!: string;
}
