-- Update default restaurant commission rate to 20% (must be > 18%)
-- Note: This only affects new restaurants. Existing restaurants retain their rates.
ALTER TABLE restaurant ALTER COLUMN restaurant_share_rate SET DEFAULT 0.2000;

-- Add a constraint to ensure commission rate is greater than 18%
ALTER TABLE restaurant ADD CONSTRAINT check_commission_rate_minimum 
    CHECK (restaurant_share_rate IS NULL OR restaurant_share_rate > 0.18);
