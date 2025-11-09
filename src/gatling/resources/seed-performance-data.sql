-- Performance Test Data Seed Script
-- This script creates sample data for performance testing
-- Run this against your test database before running performance tests

-- Note: Adjust this script based on your actual schema
-- This is a template showing the types of data needed

DO $$
DECLARE
    test_user_id BIGINT;
BEGIN
    -- Create test user for performance tests
    -- Password is bcrypt hash of 'password123'
    INSERT INTO app_users (email, password, name, role, phone_number, created_at, updated_at, enabled)
    VALUES (
        'test@foodify.com',
        '$2a$10$YqY5vJKzQZfZJrKXvCDqNuWxWLJPfCkdNKQKxf3wYxqyxYXGmMXbC',
        'Performance Test User',
        'CLIENT',
        '+1234567890',
        NOW(),
        NOW(),
        TRUE
    )
    ON CONFLICT (email) DO NOTHING
    RETURNING id INTO test_user_id;

    -- Log the result
    IF test_user_id IS NOT NULL THEN
        RAISE NOTICE 'Created test user with ID: %', test_user_id;
    ELSE
        RAISE NOTICE 'Test user already exists';
        SELECT id INTO test_user_id FROM app_users WHERE email = 'test@foodify.com';
    END IF;

    -- Create additional test users if needed
    FOR i IN 1..100 LOOP
        INSERT INTO app_users (email, password, name, role, phone_number, created_at, updated_at, enabled)
        VALUES (
            'perftest' || i || '@foodify.com',
            '$2a$10$YqY5vJKzQZfZJrKXvCDqNuWxWLJPfCkdNKQKxf3wYxqyxYXGmMXbC',
            'Perf Test User ' || i,
            CASE WHEN i % 10 = 0 THEN 'DRIVER' WHEN i % 20 = 0 THEN 'RESTAURANT_ADMIN' ELSE 'CLIENT' END,
            '+123456' || LPAD(i::text, 4, '0'),
            NOW(),
            NOW(),
            TRUE
        )
        ON CONFLICT (email) DO NOTHING;
    END LOOP;

    RAISE NOTICE 'Created 100 additional test users';

    -- Create sample restaurants (adjust based on your actual schema)
    -- Example: Assumes restaurants table exists with these columns
    /*
    FOR i IN 1..100 LOOP
        INSERT INTO restaurants (name, description, address, latitude, longitude, cuisine_type, rating, created_at, updated_at)
        VALUES (
            'Test Restaurant ' || i,
            'Performance test restaurant for load testing',
            i || ' Test Street, Test City',
            36.8065 + (RANDOM() * 0.1 - 0.05), -- Random location near Tunis
            10.1815 + (RANDOM() * 0.1 - 0.05),
            CASE (i % 5) 
                WHEN 0 THEN 'ITALIAN'
                WHEN 1 THEN 'CHINESE'
                WHEN 2 THEN 'MEXICAN'
                WHEN 3 THEN 'INDIAN'
                ELSE 'AMERICAN'
            END,
            3.5 + (RANDOM() * 1.5), -- Rating between 3.5 and 5.0
            NOW(),
            NOW()
        )
        ON CONFLICT DO NOTHING;
    END LOOP;

    RAISE NOTICE 'Created 100 sample restaurants';
    */

    RAISE NOTICE 'Performance test data seeding completed';
    RAISE NOTICE 'Test user credentials: test@foodify.com / password123';
END $$;
