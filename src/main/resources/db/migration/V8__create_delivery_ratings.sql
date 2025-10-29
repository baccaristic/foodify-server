CREATE TABLE IF NOT EXISTS delivery_ratings (
    delivery_id BIGINT PRIMARY KEY,
    timing_rating INTEGER NOT NULL,
    food_condition_rating INTEGER NOT NULL,
    professionalism_rating INTEGER NOT NULL,
    overall_rating INTEGER NOT NULL,
    comments VARCHAR(1024),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_delivery_ratings_delivery FOREIGN KEY (delivery_id) REFERENCES delivery (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_delivery_ratings_overall_rating ON delivery_ratings (overall_rating);
