# Restaurant Seed Data

This directory contains SQL seed data for populating the database with 100 fully detailed restaurants.

## Contents

- `V100__insert_100_restaurants.sql` - Main SQL script that inserts:
  - 100 restaurants with complete details
  - Restaurants spread across Tunisia (Ariana, Aouina, Menzah)
  - Multi-language support (English, French, Arabic)
  - Restaurant categories
  - 306 menu items (3-4 per restaurant)
  - Menu categories
  - 1,612 option groups
  - 2,836 menu item extras
  - All IDs start from 1000

## Images

The script references images stored in the `uploads/` directory:

- **Restaurant images**: `/uploads/restaurants/`
  - Main images: `restaurant_1000.jpg` through `restaurant_1099.jpg`
  - Icon images: `restaurant_1000_icon.jpg` through `restaurant_1099_icon.jpg`

- **Menu item images**: `/uploads/menu-items/`
  - Item images: `item_1000.jpg` through `item_1399.jpg` (400 images)

## Database Schema

The script populates the following tables:

1. **restaurant** - Main restaurant data with translations
2. **restaurant_category** - Restaurant categories (PIZZA, BURGERS, ASIAN, etc.)
3. **menu_category** - Categories for menu items within each restaurant
4. **menu_item** - Individual menu items with prices and translations
5. **menu_item_images** - Images for menu items
6. **menu_item_categories** - Link between menu items and categories
7. **menu_option_group** - Option groups (e.g., "Choose your size", "Extra toppings")
8. **menu_item_extra** - Individual extras/options with prices

## Running the Script

### Option 1: As a Flyway Migration (Recommended for Testing Only)

**Note**: This will run automatically on application startup if placed in the migration directory, but this is NOT recommended for production use.

```bash
# Copy to migrations directory (for testing only)
cp src/main/resources/db/seed/V100__insert_100_restaurants.sql src/main/resources/db/migration/
```

Then start the application and Flyway will execute it.

### Option 2: Manual Execution (Recommended)

Run the script manually using psql:

```bash
# Using psql
psql -U foodify -d foodify -f src/main/resources/db/seed/V100__insert_100_restaurants.sql

# Or using Docker
docker exec -i <postgres-container> psql -U foodify -d foodify < src/main/resources/db/seed/V100__insert_100_restaurants.sql
```

### Option 3: Using a Database Tool

1. Open your preferred database tool (DBeaver, pgAdmin, etc.)
2. Connect to your foodify database
3. Open and execute the SQL script

## Data Details

### Restaurants
- **IDs**: 1000-1099
- **Locations**: 
  - Ariana (Centre, Ville, Essoughra, Riadh)
  - Aouina (Centre, Nord, Sud)
  - Menzah (5, 6, 7, 8, 9)
- **Categories**: Distributed across 26 categories (PIZZA, BURGERS, ASIAN, ITALIAN, TUNISIAN, etc.)
- **Ratings**: Random between 3.5 and 5.0 stars
- **Delivery fees**: Random between 1.00 and 5.00 TND
- **Hours**: All open 08:00-23:00 (can be customized)

### Menu Items
- **IDs**: 1000-1305
- **Count**: 3-4 items per restaurant (306 total)
- **Prices**: Vary by category (typically 4.50-22.00 TND)
- **Translations**: All items have English, French, and Arabic names and descriptions

### Option Groups & Extras
- **Size options**: Small (default, +0.00), Medium (+2.00), Large (+4.00)
- **Toppings**: Extra Cheese (+1.50), Mushrooms (+1.00), Olives (+0.80)
- Each menu item has 2 option groups by default

## Customization

To modify the seed data:

1. Edit the generation script: `/tmp/generate_seed_data.py`
2. Adjust parameters:
   - Number of restaurants
   - Locations
   - Menu items per restaurant
   - Price ranges
   - Categories
3. Regenerate: `python3 /tmp/generate_seed_data.py`

## Important Notes

1. **IDs Start at 1000**: All IDs (restaurants, menu items, categories, etc.) start at 1000 to avoid conflicts with existing data
2. **Sequences Updated**: The script updates all relevant sequences at the end
3. **Foreign Keys**: All foreign key relationships are properly maintained
4. **Images**: Placeholder images are generated automatically
5. **Translations**: All text fields include English, French, and Arabic translations

## Verifying the Data

After running the script:

```sql
-- Check restaurant count
SELECT COUNT(*) FROM restaurant WHERE id >= 1000;  -- Should return 100

-- Check menu items
SELECT COUNT(*) FROM menu_item WHERE id >= 1000;  -- Should return 306

-- Check option groups
SELECT COUNT(*) FROM menu_option_group WHERE id >= 1000;  -- Should return 1612

-- Check extras
SELECT COUNT(*) FROM menu_item_extra WHERE id >= 1000;  -- Should return 2836

-- Check a sample restaurant with all related data
SELECT r.name, r.name_en, r.name_fr, r.name_ar,
       COUNT(DISTINCT mi.id) as menu_items,
       COUNT(DISTINCT mog.id) as option_groups,
       COUNT(DISTINCT mie.id) as extras
FROM restaurant r
LEFT JOIN menu_item mi ON mi.restaurant_id = r.id
LEFT JOIN menu_option_group mog ON mog.menu_item_id = mi.id
LEFT JOIN menu_item_extra mie ON mie.option_group_id = mog.id
WHERE r.id = 1000
GROUP BY r.id, r.name, r.name_en, r.name_fr, r.name_ar;
```

## Troubleshooting

### Error: duplicate key value violates unique constraint

If you get a duplicate key error, the sequences might need to be reset. Run:

```sql
SELECT setval('restaurant_id_seq', (SELECT MAX(id) FROM restaurant) + 1);
SELECT setval('menu_item_id_seq', (SELECT MAX(id) FROM menu_item) + 1);
SELECT setval('menu_category_id_seq', (SELECT MAX(id) FROM menu_category) + 1);
SELECT setval('menu_option_group_id_seq', (SELECT MAX(id) FROM menu_option_group) + 1);
SELECT setval('menu_item_extra_id_seq', (SELECT MAX(id) FROM menu_item_extra) + 1);
```

### Images Not Loading

Make sure the `uploads/` directory is properly configured in your application and contains the generated images.

## Production Use

For production deployment:

1. Review and customize the data to match your actual business needs
2. Replace placeholder images with real restaurant and food photos
3. Update restaurant names, descriptions, and locations
4. Adjust prices and menu items
5. Consider loading data through the application API instead of direct SQL insertion
6. Use a dedicated data import service for better error handling and validation
