-- Add financial tracking columns for drivers
ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS cash_on_hand NUMERIC(19,2) NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS unpaid_earnings NUMERIC(19,2) NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS outstanding_daily_fees NUMERIC(19,2) NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS outstanding_daily_fee_days INTEGER NOT NULL DEFAULT 0;

ALTER TABLE app_users
    ADD COLUMN IF NOT EXISTS last_daily_fee_date DATE;

-- Initialize outstanding_daily_fee_days from existing outstanding_daily_fees (if any data exists)
UPDATE app_users
SET outstanding_daily_fee_days = GREATEST(0, TRUNC(outstanding_daily_fees / 20)::INTEGER)
WHERE dtype = 'Driver' AND outstanding_daily_fees > 0;

-- Normalize outstanding_daily_fees to be multiples of 20
UPDATE app_users
SET outstanding_daily_fees = (outstanding_daily_fee_days * 20)::NUMERIC(19, 2)
WHERE dtype = 'Driver';
