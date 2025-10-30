ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS outstanding_daily_fee_days INTEGER NOT NULL DEFAULT 0;

UPDATE app_users
SET outstanding_daily_fee_days = GREATEST(0, TRUNC(outstanding_daily_fees / 20)::INTEGER)
WHERE dtype = 'Driver';

UPDATE app_users
SET outstanding_daily_fees = (outstanding_daily_fee_days * 20)::NUMERIC(19, 2)
WHERE dtype = 'Driver';
