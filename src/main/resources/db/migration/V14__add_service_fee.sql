ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS service_fee NUMERIC(12, 2) DEFAULT 0;

CREATE TABLE IF NOT EXISTS service_fee_settings (
    id BIGINT PRIMARY KEY,
    amount NUMERIC(12, 2) NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(128)
);

INSERT INTO service_fee_settings (id, amount, updated_at, updated_by)
VALUES (1, 0.00, NOW(), 'system')
ON CONFLICT (id) DO NOTHING;

UPDATE orders
SET service_fee = 0
WHERE service_fee IS NULL;
