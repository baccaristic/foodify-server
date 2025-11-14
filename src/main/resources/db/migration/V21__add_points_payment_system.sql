-- Add points_balance column to restaurant table
ALTER TABLE restaurant
ADD COLUMN IF NOT EXISTS points_balance NUMERIC(19, 2) NOT NULL DEFAULT 0;

-- Create points_payments table
CREATE TABLE IF NOT EXISTS points_payments (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    client_id BIGINT,
    amount_tnd NUMERIC(19, 2) NOT NULL,
    points_amount NUMERIC(19, 2) NOT NULL,
    payment_token VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_points_payment_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
    CONSTRAINT fk_points_payment_client FOREIGN KEY (client_id) REFERENCES app_users(id),
    CONSTRAINT chk_amount_tnd_positive CHECK (amount_tnd > 0),
    CONSTRAINT chk_points_amount_positive CHECK (points_amount > 0)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_points_payments_restaurant ON points_payments(restaurant_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_points_payments_client ON points_payments(client_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_points_payments_status ON points_payments(status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_points_payments_token ON points_payments(payment_token);
CREATE INDEX IF NOT EXISTS idx_points_payments_expires_at ON points_payments(expires_at);
