-- Add sponsored flag and position to restaurant table
ALTER TABLE restaurant ADD COLUMN sponsored BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE restaurant ADD COLUMN position INTEGER;

-- Create unique constraint on position (null values are allowed and don't count as duplicates)
CREATE UNIQUE INDEX idx_restaurant_position ON restaurant (position) WHERE position IS NOT NULL;

-- Create index for sponsored flag to optimize queries
CREATE INDEX idx_restaurant_sponsored ON restaurant (sponsored);
