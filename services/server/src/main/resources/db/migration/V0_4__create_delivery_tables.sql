CREATE TABLE IF NOT EXISTS driver_shifts (
    id BIGSERIAL PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    status VARCHAR(32),
    started_at TIMESTAMPTZ,
    finishable_at TIMESTAMPTZ,
    ended_at TIMESTAMPTZ,
    CONSTRAINT fk_driver_shifts_driver FOREIGN KEY (driver_id) REFERENCES app_users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_driver_shifts_driver ON driver_shifts (driver_id);

CREATE TABLE IF NOT EXISTS driver_shift_balances (
    id BIGSERIAL PRIMARY KEY,
    shift_id BIGINT NOT NULL UNIQUE,
    total_amount NUMERIC(19,2) NOT NULL DEFAULT 0,
    driver_share NUMERIC(19,2) NOT NULL DEFAULT 0,
    restaurant_share NUMERIC(19,2) NOT NULL DEFAULT 0,
    settled BOOLEAN NOT NULL DEFAULT FALSE,
    settled_at TIMESTAMPTZ,
    CONSTRAINT fk_driver_shift_balances_shift FOREIGN KEY (shift_id) REFERENCES driver_shifts (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS delivery (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    driver_id BIGINT,
    shift_id BIGINT,
    delivery_time BIGINT,
    time_to_pick_up BIGINT,
    assigned_time TIMESTAMPTZ,
    pickup_time TIMESTAMPTZ,
    delivered_time TIMESTAMPTZ,
    CONSTRAINT fk_delivery_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_driver FOREIGN KEY (driver_id) REFERENCES app_users (id) ON DELETE SET NULL,
    CONSTRAINT fk_delivery_shift FOREIGN KEY (shift_id) REFERENCES driver_shifts (id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_delivery_driver ON delivery (driver_id);
CREATE INDEX IF NOT EXISTS idx_delivery_shift ON delivery (shift_id);
