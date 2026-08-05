import { render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { OrderForm } from './OrderForm';

describe('OrderForm', () => {
  it('renders accessible fields and action', () => {
    render(<OrderForm onSubmit={vi.fn()} />);
    expect(screen.getByRole('textbox', { name: /cliente/i })).toBeInTheDocument();
    expect(screen.getByRole('spinbutton', { name: /total/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /criar pedido/i })).toBeInTheDocument();
  });
});
