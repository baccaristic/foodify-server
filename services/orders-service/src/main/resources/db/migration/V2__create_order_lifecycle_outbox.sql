CREATE TABLE IF NOT EXISTS order_lifecycle_outbox (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    order_id BIGINT NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    available_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_attempt_at TIMESTAMP WITHOUT TIME ZONE,
    dispatched_at TIMESTAMP WITHOUT TIME ZONE,
    attempts INTEGER NOT NULL DEFAULT 0,
    last_error TEXT
);

CREATE INDEX IF NOT EXISTS idx_order_lifecycle_outbox_status_available
    ON order_lifecycle_outbox (status, available_at);
