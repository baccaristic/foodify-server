CREATE TABLE IF NOT EXISTS phone_signup_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(32) NOT NULL,
    verification_code VARCHAR(12) NOT NULL,
    code_expires_at TIMESTAMPTZ NOT NULL,
    last_code_sent_at TIMESTAMPTZ NOT NULL,
    failed_attempt_count INTEGER NOT NULL DEFAULT 0,
    resend_count INTEGER NOT NULL DEFAULT 0,
    phone_verified_at TIMESTAMPTZ,
    email VARCHAR(160),
    email_captured_at TIMESTAMPTZ,
    first_name VARCHAR(80),
    last_name VARCHAR(80),
    name_captured_at TIMESTAMPTZ,
    terms_accepted_at TIMESTAMPTZ,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_devices (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    device_token VARCHAR(255),
    platform VARCHAR(50),
    device_id VARCHAR(255),
    app_version VARCHAR(50),
    last_seen TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_devices_user FOREIGN KEY (user_id) REFERENCES app_users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_devices_user ON user_devices (user_id);
