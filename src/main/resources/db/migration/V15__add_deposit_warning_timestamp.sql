-- Add deposit warning timestamp to driver table
ALTER TABLE app_users ADD COLUMN IF NOT EXISTS deposit_warning_sent_at TIMESTAMP;
