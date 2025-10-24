CREATE TABLE IF NOT EXISTS coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(64) NOT NULL UNIQUE,
    type VARCHAR(32) NOT NULL,
    discount_percent NUMERIC(5,2),
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    points_cost NUMERIC(19,2),
    created_from_points BOOLEAN NOT NULL DEFAULT FALSE,
    created_for_client_id BIGINT,
    CONSTRAINT fk_coupon_created_for_client FOREIGN KEY (created_for_client_id) REFERENCES app_users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS coupon_assignments (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_coupon_assignment_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_assignment_client FOREIGN KEY (client_id) REFERENCES app_users (id) ON DELETE CASCADE,
    CONSTRAINT uk_coupon_assignment UNIQUE (coupon_id, client_id)
);

CREATE TABLE IF NOT EXISTS coupon_redemptions (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    order_id BIGINT,
    redeemed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_coupon_redemption_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_redemption_client FOREIGN KEY (client_id) REFERENCES app_users (id) ON DELETE CASCADE,
    CONSTRAINT fk_coupon_redemption_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE SET NULL,
    CONSTRAINT uk_coupon_redemption UNIQUE (coupon_id, client_id)
);

CREATE INDEX IF NOT EXISTS idx_coupon_redemptions_order ON coupon_redemptions (order_id);

CREATE TABLE IF NOT EXISTS loyalty_point_transactions (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    order_id BIGINT,
    transaction_type VARCHAR(32) NOT NULL,
    points NUMERIC(19,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_loyalty_transaction_client FOREIGN KEY (client_id) REFERENCES app_users (id) ON DELETE CASCADE,
    CONSTRAINT fk_loyalty_transaction_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_loyalty_transactions_client ON loyalty_point_transactions (client_id);

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS loyalty_points_balance NUMERIC(19,2) NOT NULL DEFAULT 0;

ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS coupon_id BIGINT,
    ADD COLUMN IF NOT EXISTS coupon_discount NUMERIC(12,2),
    ADD COLUMN IF NOT EXISTS loyalty_points_earned NUMERIC(19,2);

ALTER TABLE orders
    ADD CONSTRAINT IF NOT EXISTS fk_orders_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_orders_coupon ON orders (coupon_id);
