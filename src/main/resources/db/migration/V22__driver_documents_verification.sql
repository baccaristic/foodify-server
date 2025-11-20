ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS verification_status VARCHAR(30) NOT NULL DEFAULT 'APPROVED',
    ADD COLUMN IF NOT EXISTS verification_completed_at TIMESTAMPTZ;

UPDATE app_users
SET verification_completed_at = COALESCE(verification_completed_at, NOW())
WHERE dtype = 'Driver' AND verification_status = 'APPROVED';

CREATE TABLE IF NOT EXISTS driver_documents (
    id BIGSERIAL PRIMARY KEY,
    driver_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    document_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    image_url TEXT NOT NULL,
    rejection_reason TEXT,
    reviewed_by_admin_id BIGINT,
    reviewed_by_admin_name VARCHAR(255),
    uploaded_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reviewed_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_driver_documents_driver_type
    ON driver_documents(driver_id, document_type);
