CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT,
    restaurant_id BIGINT,
    delivery_address TEXT,
    payment_method VARCHAR(100),
    lng DOUBLE PRECISION,
    lat DOUBLE PRECISION,
    saved_address_id UUID,
    order_time TIMESTAMPTZ,
    status VARCHAR(32),
    date TIMESTAMPTZ,
    archived_at TIMESTAMPTZ,
    pending_driver_id BIGINT,
    pickup_token VARCHAR(100),
    delivery_token VARCHAR(100),
    items_subtotal NUMERIC(12,2),
    extras_total NUMERIC(12,2),
    promotion_discount NUMERIC(12,2),
    items_total NUMERIC(12,2),
    delivery_fee NUMERIC(12,2),
    order_total NUMERIC(12,2),
    CONSTRAINT fk_orders_client FOREIGN KEY (client_id) REFERENCES app_users (id) ON DELETE SET NULL,
    CONSTRAINT fk_orders_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE SET NULL,
    CONSTRAINT fk_orders_saved_address FOREIGN KEY (saved_address_id) REFERENCES saved_addresses (id) ON DELETE SET NULL,
    CONSTRAINT fk_orders_pending_driver FOREIGN KEY (pending_driver_id) REFERENCES app_users (id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_orders_client ON orders (client_id);
CREATE INDEX IF NOT EXISTS idx_orders_restaurant ON orders (restaurant_id);
CREATE INDEX IF NOT EXISTS idx_orders_saved_address ON orders (saved_address_id);

CREATE TABLE IF NOT EXISTS order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT,
    special_instructions TEXT,
    quantity INTEGER NOT NULL,
    unit_base_price NUMERIC(12,2),
    unit_price NUMERIC(12,2),
    unit_extras_price NUMERIC(12,2),
    line_subtotal NUMERIC(12,2),
    promotion_discount NUMERIC(12,2),
    line_items_total NUMERIC(12,2),
    extras_total NUMERIC(12,2),
    line_total NUMERIC(12,2),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_item (id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_order_item_order ON order_item (order_id);

CREATE TABLE IF NOT EXISTS order_item_menu_item_extras (
    order_item_id BIGINT NOT NULL,
    menu_item_extras_id BIGINT NOT NULL,
    PRIMARY KEY (order_item_id, menu_item_extras_id),
    CONSTRAINT fk_order_item_menu_item_extras_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_item (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_menu_item_extras_extra
        FOREIGN KEY (menu_item_extras_id) REFERENCES menu_item_extra (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_status_history (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    action VARCHAR(64),
    previous_status VARCHAR(32),
    new_status VARCHAR(32),
    changed_by VARCHAR(255),
    reason VARCHAR(255),
    metadata TEXT,
    changed_at TIMESTAMPTZ,
    CONSTRAINT fk_order_status_history_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_order_status_history_order ON order_status_history (order_id);
