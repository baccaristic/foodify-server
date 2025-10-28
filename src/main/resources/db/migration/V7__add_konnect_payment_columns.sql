ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS payment_reference VARCHAR(128),
    ADD COLUMN IF NOT EXISTS payment_url TEXT,
    ADD COLUMN IF NOT EXISTS payment_status VARCHAR(64),
    ADD COLUMN IF NOT EXISTS payment_environment VARCHAR(32),
    ADD COLUMN IF NOT EXISTS payment_expires_at TIMESTAMP;
