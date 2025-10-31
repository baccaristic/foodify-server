-- Convert stored restaurant share rates into commission percentages (driver + platform)
-- and align the default with the new commission-based model.

-- Flip existing share rates (where restaurants previously held >50% of the order value)
-- so the column now represents the commission portion taken from the order subtotal.
UPDATE restaurant
SET restaurant_share_rate = ROUND(1 - restaurant_share_rate, 4)
WHERE restaurant_share_rate IS NOT NULL
  AND restaurant_share_rate > 0.5;

-- Default any missing values to the baseline commission (17%).
UPDATE restaurant
SET restaurant_share_rate = 0.1700
WHERE restaurant_share_rate IS NULL;

-- Ensure future inserts default to the 17% total commission expectation.
ALTER TABLE restaurant
    ALTER COLUMN restaurant_share_rate SET DEFAULT 0.1700;
