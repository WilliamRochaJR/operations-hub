CREATE TABLE audit_entries (
  event_id UUID PRIMARY KEY,
  order_id UUID NOT NULL,
  event_type VARCHAR(120) NOT NULL,
  occurred_at TIMESTAMPTZ NOT NULL,
  correlation_id VARCHAR(120) NOT NULL,
  payload TEXT NOT NULL
);

CREATE INDEX idx_audit_order_occurred ON audit_entries (order_id, occurred_at);
