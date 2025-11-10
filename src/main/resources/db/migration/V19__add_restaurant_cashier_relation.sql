-- Add restaurant_id column to app_users table for RestaurantCashier relationship
ALTER TABLE app_users 
ADD COLUMN restaurant_id BIGINT;

-- Add foreign key constraint
ALTER TABLE app_users
ADD CONSTRAINT fk_restaurant_cashier_restaurant
FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE;

-- Add index for better query performance
CREATE INDEX idx_app_users_restaurant_id ON app_users (restaurant_id);
