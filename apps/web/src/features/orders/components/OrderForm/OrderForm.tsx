import { useState, type FormEvent } from 'react';
import { Button } from '../../../../components/ui/Button/Button';
import type { CreateOrderInput } from '../../services/orders.service';

interface Props {
  disabled?: boolean;
  onSubmit: (input: CreateOrderInput) => void;
}

export function OrderForm({ disabled, onSubmit }: Props) {
  const [customerName, setCustomerName] = useState('Cliente demonstração');
  const [totalAmount, setTotalAmount] = useState('149.90');

  function submit(event: FormEvent) {
    event.preventDefault();
    onSubmit({ customerName, totalAmount: Number(totalAmount), currency: 'BRL' });
  }

  return (
    <form className="order-form" onSubmit={submit}>
      <label>
        Cliente
        <input required value={customerName} onChange={(event) => setCustomerName(event.target.value)} />
      </label>
      <label>
        Total
        <input required min="0.01" step="0.01" type="number" value={totalAmount} onChange={(event) => setTotalAmount(event.target.value)} />
      </label>
      <Button disabled={disabled} type="submit">{disabled ? 'Criando…' : 'Criar pedido'}</Button>
    </form>
  );
}
