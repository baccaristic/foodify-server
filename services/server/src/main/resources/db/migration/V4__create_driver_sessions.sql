CREATE TABLE IF NOT EXISTS driver_sessions (
    id BIGSERIAL PRIMARY KEY,
    driver_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    session_token VARCHAR(64) NOT NULL UNIQUE,
    device_id VARCHAR(255),
    status VARCHAR(32) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_heartbeat_at TIMESTAMP WITH TIME ZONE,
    ended_at TIMESTAMP WITH TIME ZONE,
    end_reason VARCHAR(64)
);

CREATE INDEX IF NOT EXISTS idx_driver_sessions_driver_active
    ON driver_sessions (driver_id)
    WHERE status = 'ACTIVE';
