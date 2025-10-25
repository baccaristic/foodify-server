CREATE TABLE IF NOT EXISTS app_users (
    id BIGSERIAL PRIMARY KEY,
    dtype VARCHAR(31) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    auth_provider VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    role VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(50),
    phone_verified BOOLEAN,
    email_verified BOOLEAN,
    google_id VARCHAR(255),
    phone VARCHAR(50),
    available BOOLEAN DEFAULT FALSE
);
