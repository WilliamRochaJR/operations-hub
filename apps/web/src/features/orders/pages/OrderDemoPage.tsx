import { useCreateOrder, useOrderAudit } from '../hooks/use-create-order';
import { OrderForm } from '../components/OrderForm/OrderForm';

export function OrderDemoPage() {
  const createOrder = useCreateOrder();
  const audit = useOrderAudit(createOrder.data?.id);

  return (
    <main className="shell">
      <p className="eyebrow">Walking skeleton · ambiente efêmero AWS</p>
      <h1>Operations Hub</h1>
      <p className="lede">Crie um pedido e valide, de ponta a ponta, o evento atravessando PostgreSQL, Kafka e o serviço de auditoria.</p>
      <section className="card" aria-labelledby="create-title">
        <h2 id="create-title">Novo pedido</h2>
        <OrderForm disabled={createOrder.isPending} onSubmit={(input) => createOrder.mutate(input)} />
        {createOrder.isError && <p role="alert" className="error">{createOrder.error.message}</p>}
      </section>
      {createOrder.data && (
        <section className="card result" aria-live="polite">
          <h2>Fluxo integrado</h2>
          <dl>
            <div><dt>Pedido</dt><dd>{createOrder.data.number}</dd></div>
            <div><dt>Status</dt><dd>{createOrder.data.status}</dd></div>
            <div><dt>Kafka</dt><dd>{audit.data?.length ? 'Evento processado' : 'Aguardando auditoria…'}</dd></div>
          </dl>
        </section>
      )}
    </main>
  );
}
