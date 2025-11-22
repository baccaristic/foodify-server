ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS consecutive_declines INTEGER NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS total_declines INTEGER NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS total_order_offers INTEGER NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS decline_blocked_until TIMESTAMP;

UPDATE app_users
SET consecutive_declines = 0,
    total_declines = 0,
    total_order_offers = 0
WHERE dtype = 'Driver' AND (consecutive_declines IS NULL OR total_declines IS NULL OR total_order_offers IS NULL);
