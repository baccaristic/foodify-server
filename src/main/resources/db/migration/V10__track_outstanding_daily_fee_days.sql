ALTER TABLE drivers
    ADD COLUMN outstanding_daily_fee_days INTEGER NOT NULL DEFAULT 0;

UPDATE drivers
SET outstanding_daily_fee_days = GREATEST(0, TRUNC(outstanding_daily_fees / 20)::INTEGER);

UPDATE drivers
SET outstanding_daily_fees = (outstanding_daily_fee_days * 20)::NUMERIC(19, 2);
