CREATE TABLE IF NOT EXISTS saved_addresses (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(32) NOT NULL,
    label VARCHAR(255),
    latitude NUMERIC(9,6),
    longitude NUMERIC(9,6),
    geohash VARCHAR(255),
    formatted_address TEXT NOT NULL,
    place_id VARCHAR(255),
    entrance_preference VARCHAR(255),
    entrance_notes TEXT,
    directions TEXT,
    internal_notes TEXT,
    is_primary BOOLEAN DEFAULT FALSE,
    type_details TEXT,
    metadata TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_saved_addresses_user FOREIGN KEY (user_id) REFERENCES app_users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_saved_addresses_user ON saved_addresses (user_id);
