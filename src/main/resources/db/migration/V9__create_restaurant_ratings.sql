CREATE TABLE IF NOT EXISTS restaurant_ratings (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL UNIQUE,
    thumbs_up BOOLEAN NOT NULL,
    comments VARCHAR(1024),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_restaurant_ratings_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
    CONSTRAINT fk_restaurant_ratings_client FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE,
    CONSTRAINT fk_restaurant_ratings_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_restaurant_ratings_restaurant ON restaurant_ratings (restaurant_id);
CREATE INDEX IF NOT EXISTS idx_restaurant_ratings_client ON restaurant_ratings (client_id);
