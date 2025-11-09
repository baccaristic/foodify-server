# Test Data for Gatling Performance Tests

This directory contains test data used by Gatling simulations.

## Test Users

For authentication in performance tests, you'll need to create test users in the database.

### Creating Test Users

You can create test users either:

1. **Via API** (after starting the server):
   ```bash
   curl -X POST http://localhost:8081/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "email": "test@foodify.com",
       "password": "password123",
       "name": "Performance Test User",
       "role": "CLIENT"
     }'
   ```

2. **Via SQL** (direct database insert):
   ```sql
   -- Connect to the database
   docker exec -it postgres psql -U foodify -d foodify
   
   -- Insert test user (adjust as needed)
   INSERT INTO app_users (email, password, name, role, created_at, updated_at)
   VALUES (
     'test@foodify.com',
     '$2a$10$YourBcryptHashHere', -- Hash for 'password123'
     'Performance Test User',
     'CLIENT',
     NOW(),
     NOW()
   );
   ```

3. **Via Application Startup** (recommended for consistent setup):
   Create a data initialization script in the application.

### Test Data Configuration

The default test configuration uses:
- **Email**: `test@foodify.com`
- **Password**: `password123`

Override these via system properties:
```bash
./gradlew runLoadTest \
  -Dperf.testUserEmail=mytest@example.com \
  -Dperf.testUserPassword=MySecurePassword
```

## Restaurant Test Data

Performance tests reference restaurants by ID (1-100). Ensure your test database has:
- At least 100 restaurants
- Restaurants with realistic data (menu items, ratings, locations)
- Geographic distribution for testing location-based queries

## Order Test Data

For order-related scenarios, ensure:
- At least 1000 historical orders
- Orders distributed across users
- Orders in various states (completed, pending, cancelled)

## Sample Data Script

You can create a data seeding script to populate test data. Example locations:
- `src/main/resources/db/migration/V999__seed_performance_test_data.sql`
- Or a separate data seeding tool

### Minimum Data Requirements

For realistic performance testing:
- **Users**: 10,000+
- **Restaurants**: 100+
- **Menu Items**: 1,000+
- **Orders**: 50,000+
- **Addresses**: 20,000+

This ensures database queries operate at realistic scale.
