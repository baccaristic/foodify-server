-- Seed data for 100 restaurants with full details
-- IDs start from 1000
-- This script includes restaurants, menu items, categories, option groups, and extras

-- Restaurant 1: Pasta House Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1000, 'Pasta House Royal', 'Pasta House Royal', 'Pasta Maison Royal', 'مطعم Pasta House', 'Rue des Jasmins, Ariana Riadh', '+216 25 925 494', 4.8, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8419, 10.1958, '/uploads/restaurants/restaurant_1000.jpg', '/uploads/restaurants/restaurant_1000_icon.jpg', TRUE, TRUE, FALSE, 2.37, '23-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1000, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1000, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1000);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1000, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1000);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1000, '/uploads/menu-items/item_1000.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1000, 1000);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1000, 1000, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1000, 1000, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1001, 1000, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1002, 1000, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1001, 1000, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1003, 1001, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1004, 1001, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1005, 1001, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1001, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1000);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1001, '/uploads/menu-items/item_1001.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1001, 1000);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1002, 1001, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1006, 1002, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1007, 1002, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1008, 1002, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1003, 1001, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1009, 1003, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1010, 1003, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1011, 1003, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1002, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1000);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1002, '/uploads/menu-items/item_1002.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1002, 1000);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1004, 1002, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1012, 1004, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1013, 1004, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1014, 1004, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1005, 1002, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1015, 1005, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1016, 1005, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1017, 1005, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 2: Pasta House Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1001, 'Pasta House Premium', 'Pasta House Premium', 'Pasta Maison Premium', 'مطعم Pasta House', 'Rue de Tunis, Aouina', '+216 21 685 663', 4.8, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1001.jpg', '/uploads/restaurants/restaurant_1001_icon.jpg', FALSE, FALSE, TRUE, 2.16, '29-51 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1001, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1001, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1001);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1003, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1001);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1003, '/uploads/menu-items/item_1003.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1003, 1001);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1006, 1003, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1018, 1006, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1019, 1006, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1020, 1006, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1007, 1003, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1021, 1007, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1022, 1007, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1023, 1007, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1004, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1001);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1004, '/uploads/menu-items/item_1004.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1004, 1001);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1008, 1004, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1024, 1008, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1025, 1008, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1026, 1008, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1009, 1004, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1027, 1009, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1028, 1009, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1029, 1009, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1005, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1001);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1005, '/uploads/menu-items/item_1005.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1005, 1001);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1010, 1005, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1030, 1010, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1031, 1010, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1032, 1010, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1011, 1005, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1033, 1011, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1034, 1011, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1035, 1011, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 3: La Boulangerie Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1002, 'La Boulangerie Plus', 'La Boulangerie Plus', 'La Boulangerie Plus', 'مطعم La Boulangerie', 'Rue des Roses, Menzah 8', '+216 23 757 251', 4.9, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1002.jpg', '/uploads/restaurants/restaurant_1002_icon.jpg', FALSE, FALSE, FALSE, 3.03, '25-43 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1002, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1002, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1002);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1006, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1002);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1006, '/uploads/menu-items/item_1006.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1006, 1002);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1012, 1006, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1036, 1012, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1037, 1012, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1038, 1012, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1013, 1006, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1039, 1013, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1040, 1013, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1041, 1013, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1007, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1002);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1007, '/uploads/menu-items/item_1007.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1007, 1002);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1014, 1007, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1042, 1014, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1043, 1014, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1044, 1014, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1015, 1007, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1045, 1015, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1046, 1015, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1047, 1015, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1008, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1002);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1008, '/uploads/menu-items/item_1008.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1008, 1002);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1016, 1008, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1048, 1016, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1049, 1016, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1050, 1016, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1017, 1008, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1051, 1017, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1052, 1017, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1053, 1017, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 4: Restaurant Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1003, 'Restaurant Premium', 'Restaurant Premium', 'Restaurant Premium', 'مطعم Restaurant', 'Avenue de la Liberté, Menzah 7', '+216 24 204 630', 4.3, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1003.jpg', '/uploads/restaurants/restaurant_1003_icon.jpg', FALSE, FALSE, FALSE, 3.92, '29-40 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1003, 'TURKISH');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1003, 'Turkish Dishes', 'Turkish Dishes', 'Plats turkish', 'أطباق TURKISH', 1003);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1009, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1003);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1009, '/uploads/menu-items/item_1009.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1009, 1003);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1018, 1009, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1054, 1018, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1055, 1018, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1056, 1018, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1019, 1009, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1057, 1019, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1058, 1019, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1059, 1019, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1010, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1003);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1010, '/uploads/menu-items/item_1010.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1010, 1003);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1020, 1010, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1060, 1020, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1061, 1020, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1062, 1020, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1021, 1010, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1063, 1021, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1064, 1021, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1065, 1021, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1011, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1003);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1011, '/uploads/menu-items/item_1011.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1011, 1003);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1022, 1011, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1066, 1022, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1067, 1022, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1068, 1022, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1023, 1011, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1069, 1023, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1070, 1023, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1071, 1023, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 5: Seafood House Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1004, 'Seafood House Premium', 'Seafood House Premium', 'Seafood Maison Premium', 'مطعم Seafood House', 'Avenue de la Liberté, Ariana Essoughra', '+216 25 676 493', 4.7, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1004.jpg', '/uploads/restaurants/restaurant_1004_icon.jpg', FALSE, FALSE, FALSE, 1.49, '28-55 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1004, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1004, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1004);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1012, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1004);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1012, '/uploads/menu-items/item_1012.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1012, 1004);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1024, 1012, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1072, 1024, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1073, 1024, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1074, 1024, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1025, 1012, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1075, 1025, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1076, 1025, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1077, 1025, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1013, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1004);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1013, '/uploads/menu-items/item_1013.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1013, 1004);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1026, 1013, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1078, 1026, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1079, 1026, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1080, 1026, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1027, 1013, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1081, 1027, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1082, 1027, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1083, 1027, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1014, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1004);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1014, '/uploads/menu-items/item_1014.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1014, 1004);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1028, 1014, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1084, 1028, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1085, 1028, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1086, 1028, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1029, 1014, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1087, 1029, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1088, 1029, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1089, 1029, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 6: Restaurant Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1005, 'Restaurant Palace', 'Restaurant Palace', 'Restaurant Palais', 'مطعم Restaurant', 'Rue des Roses, Menzah 8', '+216 21 866 865', 4.1, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1005.jpg', '/uploads/restaurants/restaurant_1005_icon.jpg', FALSE, FALSE, FALSE, 2.08, '19-47 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1005, 'ICE_CREAM');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1005, 'Ice Cream Dishes', 'Ice Cream Dishes', 'Plats ice cream', 'أطباق ICE_CREAM', 1005);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1015, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1005);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1015, '/uploads/menu-items/item_1015.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1015, 1005);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1030, 1015, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1090, 1030, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1091, 1030, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1092, 1030, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1031, 1015, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1093, 1031, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1094, 1031, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1095, 1031, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1016, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1005);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1016, '/uploads/menu-items/item_1016.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1016, 1005);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1032, 1016, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1096, 1032, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1097, 1032, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1098, 1032, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1033, 1016, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1099, 1033, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1100, 1033, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1101, 1033, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1017, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1005);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1017, '/uploads/menu-items/item_1017.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1017, 1005);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1034, 1017, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1102, 1034, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1103, 1034, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1104, 1034, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1035, 1017, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1105, 1035, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1106, 1035, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1107, 1035, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 7: Bistro Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1006, 'Bistro Palace', 'Bistro Palace', 'Bistro Palais', 'مطعم Bistro', 'Boulevard de l'Environnement, Menzah 9', '+216 28 874 722', 4.9, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8167, 10.2, '/uploads/restaurants/restaurant_1006.jpg', '/uploads/restaurants/restaurant_1006_icon.jpg', FALSE, FALSE, FALSE, 3.0, '25-53 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1006, 'INTERNATIONAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1006, 'International Dishes', 'International Dishes', 'Plats international', 'أطباق INTERNATIONAL', 1006);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1018, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1006);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1018, '/uploads/menu-items/item_1018.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1018, 1006);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1036, 1018, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1108, 1036, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1109, 1036, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1110, 1036, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1037, 1018, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1111, 1037, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1112, 1037, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1113, 1037, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1019, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1006);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1019, '/uploads/menu-items/item_1019.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1019, 1006);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1038, 1019, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1114, 1038, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1115, 1038, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1116, 1038, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1039, 1019, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1117, 1039, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1118, 1039, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1119, 1039, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1020, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1006);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1020, '/uploads/menu-items/item_1020.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1020, 1006);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1040, 1020, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1120, 1040, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1121, 1040, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1122, 1040, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1041, 1020, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1123, 1041, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1124, 1041, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1125, 1041, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 8: Carthage Resto Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1007, 'Carthage Resto Centre', 'Carthage Resto Centre', 'Carthage Resto Centre', 'مطعم Carthage Resto', 'Rue de la République, Ariana', '+216 24 709 582', 4.1, 'Authentic Tunisian cuisine with traditional recipes', 'Authentic Tunisian cuisine with traditional recipes', 'Cuisine tunisienne authentique avec des recettes traditionnelles', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1007.jpg', '/uploads/restaurants/restaurant_1007_icon.jpg', FALSE, TRUE, FALSE, 4.21, '27-40 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1007, 'TUNISIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1007, 'Tunisian Dishes', 'Tunisian Dishes', 'Plats tunisian', 'أطباق TUNISIAN', 1007);

-- Menu item: Lablabi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1021, 'Lablabi', 'Lablabi', 'Lablabi', 'لبلابي', 'Chickpea soup with bread', 'Chickpea soup with bread', 'Chickpea soup with bread', 'Chickpea soup with bread', TRUE, 4.5, TRUE, 1007);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1021, '/uploads/menu-items/item_1021.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1021, 1007);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1042, 1021, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1126, 1042, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1127, 1042, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1128, 1042, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1043, 1021, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1129, 1043, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1130, 1043, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1131, 1043, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Couscous
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1022, 'Couscous', 'Couscous', 'Couscous', 'كسكس', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', FALSE, 10.0, TRUE, 1007);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1022, '/uploads/menu-items/item_1022.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1022, 1007);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1044, 1022, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1132, 1044, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1133, 1044, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1134, 1044, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1045, 1022, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1135, 1045, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1136, 1045, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1137, 1045, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Ojja
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1023, 'Ojja', 'Ojja', 'Ojja', 'عجة', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', FALSE, 7.0, TRUE, 1007);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1023, '/uploads/menu-items/item_1023.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1023, 1007);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1046, 1023, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1138, 1046, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1139, 1046, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1140, 1046, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1047, 1023, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1141, 1047, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1142, 1047, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1143, 1047, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 9: Quick Eat Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1008, 'Quick Eat Ville', 'Quick Eat Ville', 'Quick Eat Ville', 'مطعم Quick Eat', 'Rue des Roses, Menzah 8', '+216 28 720 943', 3.9, 'Quick and tasty meals for people on the go', 'Quick and tasty meals for people on the go', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1008.jpg', '/uploads/restaurants/restaurant_1008_icon.jpg', FALSE, TRUE, FALSE, 1.93, '17-48 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1008, 'FAST_FOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1008, 'Fast Food Dishes', 'Fast Food Dishes', 'Plats fast food', 'أطباق FAST_FOOD', 1008);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1024, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1008);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1024, '/uploads/menu-items/item_1024.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1024, 1008);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1048, 1024, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1144, 1048, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1145, 1048, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1146, 1048, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1049, 1024, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1147, 1049, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1148, 1049, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1149, 1049, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1025, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1008);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1025, '/uploads/menu-items/item_1025.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1025, 1008);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1050, 1025, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1150, 1050, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1151, 1050, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1152, 1050, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1051, 1025, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1153, 1051, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1154, 1051, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1155, 1051, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1026, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1008);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1026, '/uploads/menu-items/item_1026.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1026, 1008);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1052, 1026, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1156, 1052, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1157, 1052, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1158, 1052, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1053, 1026, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1159, 1053, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1160, 1053, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1161, 1053, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 10: Eatery Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1009, 'Eatery Royal', 'Eatery Royal', 'Eatery Royal', 'مطعم Eatery', 'Avenue de la Liberté, Menzah 7', '+216 20 201 794', 3.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1009.jpg', '/uploads/restaurants/restaurant_1009_icon.jpg', FALSE, FALSE, FALSE, 4.78, '22-48 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1009, 'SNACKS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1009, 'Snacks Dishes', 'Snacks Dishes', 'Plats snacks', 'أطباق SNACKS', 1009);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1027, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1009);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1027, '/uploads/menu-items/item_1027.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1027, 1009);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1054, 1027, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1162, 1054, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1163, 1054, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1164, 1054, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1055, 1027, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1165, 1055, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1166, 1055, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1167, 1055, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1028, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1009);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1028, '/uploads/menu-items/item_1028.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1028, 1009);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1056, 1028, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1168, 1056, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1169, 1056, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1170, 1056, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1057, 1028, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1171, 1057, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1172, 1057, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1173, 1057, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1029, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1009);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1029, '/uploads/menu-items/item_1029.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1029, 1009);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1058, 1029, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1174, 1058, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1175, 1058, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1176, 1058, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1059, 1029, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1177, 1059, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1178, 1059, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1179, 1059, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 11: Tacos Corner Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1010, 'Tacos Corner Palace', 'Tacos Corner Palace', 'Tacos Coin Palais', 'مطعم Tacos Corner', 'Boulevard de l'Aéroport, Aouina', '+216 22 891 635', 4.4, 'Mexican tacos with fresh ingredients', 'Mexican tacos with fresh ingredients', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1010.jpg', '/uploads/restaurants/restaurant_1010_icon.jpg', FALSE, FALSE, FALSE, 3.46, '16-50 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1010, 'TACOS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1010, 'Tacos Dishes', 'Tacos Dishes', 'Plats tacos', 'أطباق TACOS', 1010);

-- Menu item: Fish Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1030, 'Fish Tacos', 'Fish Tacos', 'Tacos Poisson', 'تاكو بالسمك', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', TRUE, 9.0, TRUE, 1010);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1030, '/uploads/menu-items/item_1030.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1030, 1010);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1060, 1030, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1180, 1060, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1181, 1060, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1182, 1060, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1061, 1030, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1183, 1061, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1184, 1061, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1185, 1061, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Chicken Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1031, 'Chicken Tacos', 'Chicken Tacos', 'Tacos Poulet', 'تاكو بالدجاج', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', FALSE, 7.5, TRUE, 1010);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1031, '/uploads/menu-items/item_1031.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1031, 1010);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1062, 1031, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1186, 1062, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1187, 1062, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1188, 1062, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1063, 1031, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1189, 1063, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1190, 1063, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1191, 1063, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Beef Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1032, 'Beef Tacos', 'Beef Tacos', 'Tacos Boeuf', 'تاكو باللحم', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', FALSE, 8.0, TRUE, 1010);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1032, '/uploads/menu-items/item_1032.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1032, 1010);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1064, 1032, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1192, 1064, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1193, 1064, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1194, 1064, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1065, 1032, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1195, 1065, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1196, 1065, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1197, 1065, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 12: Chicken Corner Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1011, 'Chicken Corner Royal', 'Chicken Corner Royal', 'Chicken Coin Royal', 'مطعم Chicken Corner', 'Rue de Tunis, Aouina', '+216 25 618 195', 4.0, 'Grilled and fried chicken specialties', 'Grilled and fried chicken specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1011.jpg', '/uploads/restaurants/restaurant_1011_icon.jpg', FALSE, TRUE, FALSE, 4.91, '20-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1011, 'CHICKEN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1011, 'Chicken Dishes', 'Chicken Dishes', 'Plats chicken', 'أطباق CHICKEN', 1011);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1033, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1011);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1033, '/uploads/menu-items/item_1033.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1033, 1011);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1066, 1033, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1198, 1066, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1199, 1066, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1200, 1066, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1067, 1033, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1201, 1067, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1202, 1067, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1203, 1067, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1034, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1011);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1034, '/uploads/menu-items/item_1034.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1034, 1011);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1068, 1034, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1204, 1068, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1205, 1068, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1206, 1068, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1069, 1034, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1207, 1069, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1208, 1069, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1209, 1069, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1035, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1011);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1035, '/uploads/menu-items/item_1035.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1035, 1011);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1070, 1035, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1210, 1070, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1211, 1070, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1212, 1070, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1071, 1035, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1213, 1071, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1214, 1071, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1215, 1071, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 13: Café Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1012, 'Café Plus', 'Café Plus', 'Café Plus', 'مطعم Café', 'Rue des Roses, Menzah 8', '+216 23 812 504', 4.2, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1012.jpg', '/uploads/restaurants/restaurant_1012_icon.jpg', TRUE, FALSE, FALSE, 4.72, '26-38 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1012, 'INTERNATIONAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1012, 'International Dishes', 'International Dishes', 'Plats international', 'أطباق INTERNATIONAL', 1012);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1036, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1012);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1036, '/uploads/menu-items/item_1036.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1036, 1012);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1072, 1036, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1216, 1072, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1217, 1072, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1218, 1072, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1073, 1036, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1219, 1073, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1220, 1073, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1221, 1073, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1037, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1012);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1037, '/uploads/menu-items/item_1037.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1037, 1012);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1074, 1037, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1222, 1074, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1223, 1074, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1224, 1074, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1075, 1037, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1225, 1075, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1226, 1075, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1227, 1075, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1038, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1012);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1038, '/uploads/menu-items/item_1038.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1038, 1012);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1076, 1038, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1228, 1076, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1229, 1076, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1230, 1076, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1077, 1038, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1231, 1077, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1232, 1077, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1233, 1077, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 14: Pizza Palace Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1013, 'Pizza Palace Ville', 'Pizza Palace Ville', 'Pizza Palais Ville', 'مطعم Pizza Palace', 'Avenue de la Liberté, Menzah 7', '+216 26 123 957', 4.6, 'Authentic wood-fired pizzas with fresh ingredients', 'Authentic wood-fired pizzas with fresh ingredients', 'Pizzas authentiques au feu de bois avec des ingrédients frais', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1013.jpg', '/uploads/restaurants/restaurant_1013_icon.jpg', FALSE, FALSE, FALSE, 1.78, '19-57 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1013, 'PIZZA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1013, 'Pizza Dishes', 'Pizza Dishes', 'Plats pizza', 'أطباق PIZZA', 1013);

-- Menu item: Pepperoni Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1039, 'Pepperoni Pizza', 'Pepperoni Pizza', 'Pizza Pepperoni', 'بيتزا ببروني', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', TRUE, 14.0, TRUE, 1013);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1039, '/uploads/menu-items/item_1039.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1039, 1013);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1078, 1039, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1234, 1078, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1235, 1078, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1236, 1078, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1079, 1039, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1237, 1079, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1238, 1079, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1239, 1079, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Four Cheese Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1040, 'Four Cheese Pizza', 'Four Cheese Pizza', 'Pizza Quatre Fromages', 'بيتزا بأربعة أجبان', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', FALSE, 15.5, TRUE, 1013);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1040, '/uploads/menu-items/item_1040.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1040, 1013);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1080, 1040, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1240, 1080, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1241, 1080, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1242, 1080, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1081, 1040, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1243, 1081, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1244, 1081, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1245, 1081, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Margherita Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1041, 'Margherita Pizza', 'Margherita Pizza', 'Pizza Margherita', 'بيتزا مارغريتا', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', FALSE, 12.5, TRUE, 1013);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1041, '/uploads/menu-items/item_1041.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1041, 1013);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1082, 1041, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1246, 1082, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1247, 1082, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1248, 1082, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1083, 1041, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1249, 1083, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1250, 1083, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1251, 1083, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Vegetarian Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1042, 'Vegetarian Pizza', 'Vegetarian Pizza', 'Pizza Végétarienne', 'بيتزا نباتية', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', FALSE, 13.0, TRUE, 1013);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1042, '/uploads/menu-items/item_1042.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1042, 1013);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1084, 1042, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1252, 1084, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1253, 1084, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1254, 1084, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1085, 1042, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1255, 1085, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1256, 1085, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1257, 1085, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 15: Tacos Express Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1014, 'Tacos Express Centre', 'Tacos Express Centre', 'Tacos Express Centre', 'مطعم Tacos Express', 'Avenue de la Liberté, Ariana Essoughra', '+216 20 694 165', 3.8, 'Mexican tacos with fresh ingredients', 'Mexican tacos with fresh ingredients', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1014.jpg', '/uploads/restaurants/restaurant_1014_icon.jpg', FALSE, FALSE, FALSE, 2.69, '26-38 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1014, 'TACOS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1014, 'Tacos Dishes', 'Tacos Dishes', 'Plats tacos', 'أطباق TACOS', 1014);

-- Menu item: Beef Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1043, 'Beef Tacos', 'Beef Tacos', 'Tacos Boeuf', 'تاكو باللحم', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', TRUE, 8.0, TRUE, 1014);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1043, '/uploads/menu-items/item_1043.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1043, 1014);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1086, 1043, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1258, 1086, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1259, 1086, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1260, 1086, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1087, 1043, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1261, 1087, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1262, 1087, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1263, 1087, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Chicken Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1044, 'Chicken Tacos', 'Chicken Tacos', 'Tacos Poulet', 'تاكو بالدجاج', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', FALSE, 7.5, TRUE, 1014);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1044, '/uploads/menu-items/item_1044.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1044, 1014);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1088, 1044, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1264, 1088, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1265, 1088, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1266, 1088, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1089, 1044, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1267, 1089, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1268, 1089, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1269, 1089, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Fish Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1045, 'Fish Tacos', 'Fish Tacos', 'Tacos Poisson', 'تاكو بالسمك', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', FALSE, 9.0, TRUE, 1014);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1045, '/uploads/menu-items/item_1045.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1045, 1014);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1090, 1045, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1270, 1090, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1271, 1090, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1272, 1090, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1091, 1045, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1273, 1091, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1274, 1091, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1275, 1091, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 16: Le Traditionnel Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1015, 'Le Traditionnel Ville', 'Le Traditionnel Ville', 'Le Traditionnel Ville', 'مطعم Le Traditionnel', 'Avenue Mohamed V, Menzah 5', '+216 25 676 136', 3.7, 'Authentic Tunisian cuisine with traditional recipes', 'Authentic Tunisian cuisine with traditional recipes', 'Cuisine tunisienne authentique avec des recettes traditionnelles', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1015.jpg', '/uploads/restaurants/restaurant_1015_icon.jpg', FALSE, FALSE, FALSE, 3.53, '15-46 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1015, 'TUNISIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1015, 'Tunisian Dishes', 'Tunisian Dishes', 'Plats tunisian', 'أطباق TUNISIAN', 1015);

-- Menu item: Couscous
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1046, 'Couscous', 'Couscous', 'Couscous', 'كسكس', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', TRUE, 10.0, TRUE, 1015);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1046, '/uploads/menu-items/item_1046.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1046, 1015);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1092, 1046, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1276, 1092, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1277, 1092, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1278, 1092, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1093, 1046, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1279, 1093, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1280, 1093, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1281, 1093, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Ojja
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1047, 'Ojja', 'Ojja', 'Ojja', 'عجة', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', FALSE, 7.0, TRUE, 1015);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1047, '/uploads/menu-items/item_1047.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1047, 1015);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1094, 1047, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1282, 1094, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1283, 1094, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1284, 1094, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1095, 1047, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1285, 1095, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1286, 1095, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1287, 1095, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Brik
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1048, 'Brik', 'Brik', 'Brik', 'بريك', 'Crispy pastry with egg and tuna', 'Crispy pastry with egg and tuna', 'Crispy pastry with egg and tuna', 'Crispy pastry with egg and tuna', FALSE, 5.5, TRUE, 1015);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1048, '/uploads/menu-items/item_1048.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1048, 1015);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1096, 1048, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1288, 1096, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1289, 1096, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1290, 1096, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1097, 1048, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1291, 1097, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1292, 1097, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1293, 1097, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 17: Smokehouse Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1016, 'Smokehouse Plus', 'Smokehouse Plus', 'Smokehouse Plus', 'مطعم Smokehouse', 'Avenue Habib Bourguiba, Ariana', '+216 28 578 514', 4.2, 'Grilled meats and BBQ specialties', 'Grilled meats and BBQ specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1016.jpg', '/uploads/restaurants/restaurant_1016_icon.jpg', FALSE, FALSE, FALSE, 4.64, '25-45 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1016, 'GRILL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1016, 'Grill Dishes', 'Grill Dishes', 'Plats grill', 'أطباق GRILL', 1016);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1049, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1016);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1049, '/uploads/menu-items/item_1049.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1049, 1016);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1098, 1049, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1294, 1098, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1295, 1098, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1296, 1098, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1099, 1049, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1297, 1099, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1298, 1099, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1299, 1099, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1050, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1016);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1050, '/uploads/menu-items/item_1050.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1050, 1016);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1100, 1050, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1300, 1100, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1301, 1100, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1302, 1100, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1101, 1050, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1303, 1101, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1304, 1101, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1305, 1101, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1051, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1016);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1051, '/uploads/menu-items/item_1051.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1051, 1016);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1102, 1051, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1306, 1102, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1307, 1102, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1308, 1102, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1103, 1051, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1309, 1103, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1310, 1103, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1311, 1103, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 18: La Piazza Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1017, 'La Piazza Express', 'La Piazza Express', 'La Piazza Express', 'مطعم La Piazza', 'Rue de Tunis, Aouina', '+216 29 287 677', 4.9, 'Traditional Italian dishes prepared with love', 'Traditional Italian dishes prepared with love', 'Plats italiens traditionnels préparés avec amour', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1017.jpg', '/uploads/restaurants/restaurant_1017_icon.jpg', FALSE, TRUE, FALSE, 1.69, '24-44 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1017, 'ITALIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1017, 'Italian Dishes', 'Italian Dishes', 'Plats italian', 'أطباق ITALIAN', 1017);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1052, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1017);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1052, '/uploads/menu-items/item_1052.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1052, 1017);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1104, 1052, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1312, 1104, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1313, 1104, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1314, 1104, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1105, 1052, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1315, 1105, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1316, 1105, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1317, 1105, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1053, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1017);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1053, '/uploads/menu-items/item_1053.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1053, 1017);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1106, 1053, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1318, 1106, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1319, 1106, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1320, 1106, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1107, 1053, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1321, 1107, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1322, 1107, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1323, 1107, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1054, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1017);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1054, '/uploads/menu-items/item_1054.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1054, 1017);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1108, 1054, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1324, 1108, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1325, 1108, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1326, 1108, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1109, 1054, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1327, 1109, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1328, 1109, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1329, 1109, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 19: Sea Breeze Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1018, 'Sea Breeze Plus', 'Sea Breeze Plus', 'Sea Breeze Plus', 'مطعم Sea Breeze', 'Avenue de la Liberté, Menzah 7', '+216 26 115 791', 4.2, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1018.jpg', '/uploads/restaurants/restaurant_1018_icon.jpg', TRUE, FALSE, FALSE, 2.73, '21-56 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1018, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1018, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1018);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1055, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1018);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1055, '/uploads/menu-items/item_1055.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1055, 1018);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1110, 1055, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1330, 1110, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1331, 1110, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1332, 1110, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1111, 1055, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1333, 1111, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1334, 1111, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1335, 1111, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1056, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1018);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1056, '/uploads/menu-items/item_1056.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1056, 1018);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1112, 1056, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1336, 1112, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1337, 1112, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1338, 1112, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1113, 1056, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1339, 1113, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1340, 1113, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1341, 1113, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1057, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1018);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1057, '/uploads/menu-items/item_1057.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1057, 1018);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1114, 1057, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1342, 1114, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1343, 1114, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1344, 1114, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1115, 1057, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1345, 1115, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1346, 1115, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1347, 1115, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 20: Eatery Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1019, 'Eatery Palace', 'Eatery Palace', 'Eatery Palais', 'مطعم Eatery', 'Rue de Tunis, Aouina', '+216 22 734 639', 4.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1019.jpg', '/uploads/restaurants/restaurant_1019_icon.jpg', FALSE, FALSE, FALSE, 2.41, '23-55 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1019, 'SNACKS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1019, 'Snacks Dishes', 'Snacks Dishes', 'Plats snacks', 'أطباق SNACKS', 1019);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1058, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1019);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1058, '/uploads/menu-items/item_1058.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1058, 1019);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1116, 1058, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1348, 1116, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1349, 1116, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1350, 1116, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1117, 1058, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1351, 1117, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1352, 1117, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1353, 1117, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1059, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1019);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1059, '/uploads/menu-items/item_1059.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1059, 1019);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1118, 1059, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1354, 1118, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1355, 1118, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1356, 1118, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1119, 1059, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1357, 1119, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1358, 1119, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1359, 1119, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1060, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1019);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1060, '/uploads/menu-items/item_1060.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1060, 1019);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1120, 1060, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1360, 1120, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1361, 1120, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1362, 1120, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1121, 1060, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1363, 1121, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1364, 1121, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1365, 1121, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 21: Eatery Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1020, 'Eatery Ville', 'Eatery Ville', 'Eatery Ville', 'مطعم Eatery', 'Rue de la République, Ariana', '+216 25 857 905', 4.8, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1020.jpg', '/uploads/restaurants/restaurant_1020_icon.jpg', TRUE, FALSE, FALSE, 3.88, '22-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1020, 'ORIENTAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1020, 'Oriental Dishes', 'Oriental Dishes', 'Plats oriental', 'أطباق ORIENTAL', 1020);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1061, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1020);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1061, '/uploads/menu-items/item_1061.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1061, 1020);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1122, 1061, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1366, 1122, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1367, 1122, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1368, 1122, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1123, 1061, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1369, 1123, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1370, 1123, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1371, 1123, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1062, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1020);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1062, '/uploads/menu-items/item_1062.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1062, 1020);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1124, 1062, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1372, 1124, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1373, 1124, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1374, 1124, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1125, 1062, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1375, 1125, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1376, 1125, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1377, 1125, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1063, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1020);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1063, '/uploads/menu-items/item_1063.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1063, 1020);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1126, 1063, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1378, 1126, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1379, 1126, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1380, 1126, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1127, 1063, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1381, 1127, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1382, 1127, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1383, 1127, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 22: Italian Pasta Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1021, 'Italian Pasta Royal', 'Italian Pasta Royal', 'Italian Pasta Royal', 'مطعم Italian Pasta', 'Avenue de Carthage, Aouina', '+216 20 989 514', 3.6, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1021.jpg', '/uploads/restaurants/restaurant_1021_icon.jpg', TRUE, TRUE, FALSE, 2.47, '24-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1021, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1021, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1021);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1064, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1021);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1064, '/uploads/menu-items/item_1064.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1064, 1021);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1128, 1064, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1384, 1128, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1385, 1128, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1386, 1128, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1129, 1064, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1387, 1129, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1388, 1129, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1389, 1129, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1065, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1021);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1065, '/uploads/menu-items/item_1065.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1065, 1021);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1130, 1065, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1390, 1130, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1391, 1130, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1392, 1130, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1131, 1065, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1393, 1131, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1394, 1131, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1395, 1131, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1066, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1021);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1066, '/uploads/menu-items/item_1066.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1066, 1021);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1132, 1066, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1396, 1132, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1397, 1132, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1398, 1132, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1133, 1066, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1399, 1133, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1400, 1133, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1401, 1133, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 23: Seafood House Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1022, 'Seafood House Premium', 'Seafood House Premium', 'Seafood Maison Premium', 'مطعم Seafood House', 'Rue de Tunis, Aouina', '+216 21 599 194', 4.7, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1022.jpg', '/uploads/restaurants/restaurant_1022_icon.jpg', FALSE, TRUE, FALSE, 1.37, '25-47 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1022, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1022, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1022);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1067, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1022);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1067, '/uploads/menu-items/item_1067.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1067, 1022);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1134, 1067, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1402, 1134, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1403, 1134, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1404, 1134, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1135, 1067, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1405, 1135, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1406, 1135, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1407, 1135, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1068, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1022);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1068, '/uploads/menu-items/item_1068.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1068, 1022);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1136, 1068, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1408, 1136, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1409, 1136, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1410, 1136, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1137, 1068, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1411, 1137, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1412, 1137, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1413, 1137, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1069, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1022);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1069, '/uploads/menu-items/item_1069.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1069, 1022);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1138, 1069, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1414, 1138, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1415, 1138, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1416, 1138, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1139, 1069, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1417, 1139, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1418, 1139, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1419, 1139, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 24: Oriental Kitchen Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1023, 'Oriental Kitchen Palace', 'Oriental Kitchen Palace', 'Oriental Cuisine Palais', 'مطعم Oriental Kitchen', 'Boulevard de l'Aéroport, Aouina', '+216 28 742 853', 3.9, 'Delicious Asian cuisine with authentic flavors', 'Delicious Asian cuisine with authentic flavors', 'Délicieuse cuisine asiatique aux saveurs authentiques', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1023.jpg', '/uploads/restaurants/restaurant_1023_icon.jpg', TRUE, FALSE, FALSE, 3.73, '21-53 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1023, 'ASIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1023, 'Asian Dishes', 'Asian Dishes', 'Plats asian', 'أطباق ASIAN', 1023);

-- Menu item: Noodles
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1070, 'Noodles', 'Noodles', 'Nouilles', 'نودلز', 'Stir-fried noodles with sauce', 'Stir-fried noodles with sauce', 'Stir-fried noodles with sauce', 'Stir-fried noodles with sauce', TRUE, 9.0, TRUE, 1023);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1070, '/uploads/menu-items/item_1070.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1070, 1023);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1140, 1070, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1420, 1140, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1421, 1140, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1422, 1140, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1141, 1070, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1423, 1141, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1424, 1141, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1425, 1141, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Fried Rice
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1071, 'Fried Rice', 'Fried Rice', 'Riz Frit', 'أرز مقلي', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', FALSE, 8.5, TRUE, 1023);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1071, '/uploads/menu-items/item_1071.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1071, 1023);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1142, 1071, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1426, 1142, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1427, 1142, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1428, 1142, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1143, 1071, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1429, 1143, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1430, 1143, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1431, 1143, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Spring Rolls
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1072, 'Spring Rolls', 'Spring Rolls', 'Rouleaux de Printemps', 'لفائف الربيع', 'Crispy vegetable rolls', 'Crispy vegetable rolls', 'Crispy vegetable rolls', 'Crispy vegetable rolls', FALSE, 6.5, TRUE, 1023);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1072, '/uploads/menu-items/item_1072.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1072, 1023);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1144, 1072, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1432, 1144, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1433, 1144, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1434, 1144, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1145, 1072, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1435, 1145, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1436, 1145, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1437, 1145, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 25: Sakura Sushi Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1024, 'Sakura Sushi Premium', 'Sakura Sushi Premium', 'Sakura Sushi Premium', 'مطعم Sakura Sushi', 'Avenue Mohamed V, Menzah 5', '+216 29 895 941', 4.8, 'Fresh sushi and Japanese specialties', 'Fresh sushi and Japanese specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1024.jpg', '/uploads/restaurants/restaurant_1024_icon.jpg', TRUE, FALSE, TRUE, 1.45, '22-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1024, 'SUSHI');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1024, 'Sushi Dishes', 'Sushi Dishes', 'Plats sushi', 'أطباق SUSHI', 1024);

-- Menu item: Tuna Sashimi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1073, 'Tuna Sashimi', 'Tuna Sashimi', 'Sashimi Thon', 'ساشيمي التونة', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', TRUE, 16.0, TRUE, 1024);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1073, '/uploads/menu-items/item_1073.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1073, 1024);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1146, 1073, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1438, 1146, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1439, 1146, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1440, 1146, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1147, 1073, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1441, 1147, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1442, 1147, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1443, 1147, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Salmon Sushi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1074, 'Salmon Sushi', 'Salmon Sushi', 'Sushi Saumon', 'سوشي السلمون', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', FALSE, 14.0, TRUE, 1024);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1074, '/uploads/menu-items/item_1074.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1074, 1024);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1148, 1074, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1444, 1148, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1445, 1148, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1446, 1148, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1149, 1074, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1447, 1149, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1448, 1149, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1449, 1149, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Mixed Sushi Platter
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1075, 'Mixed Sushi Platter', 'Mixed Sushi Platter', 'Plateau Mixte Sushi', 'طبق سوشي مشكل', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', FALSE, 22.0, TRUE, 1024);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1075, '/uploads/menu-items/item_1075.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1075, 1024);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1150, 1075, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1450, 1150, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1451, 1150, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1452, 1150, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1151, 1075, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1453, 1151, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1454, 1151, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1455, 1151, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: California Roll
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1076, 'California Roll', 'California Roll', 'California Roll', 'كاليفورنيا رول', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', FALSE, 12.0, TRUE, 1024);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1076, '/uploads/menu-items/item_1076.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1076, 1024);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1152, 1076, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1456, 1152, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1457, 1152, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1458, 1152, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1153, 1076, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1459, 1153, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1460, 1153, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1461, 1153, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 26: Café Central Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1025, 'Café Central Palace', 'Café Central Palace', 'Café Central Palais', 'مطعم Café Central', 'Avenue de Carthage, Aouina', '+216 29 118 339', 5.0, 'Quality coffee and tea in a cozy atmosphere', 'Quality coffee and tea in a cozy atmosphere', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1025.jpg', '/uploads/restaurants/restaurant_1025_icon.jpg', FALSE, FALSE, TRUE, 3.21, '21-45 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1025, 'TEA_COFFEE');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1025, 'Tea Coffee Dishes', 'Tea Coffee Dishes', 'Plats tea coffee', 'أطباق TEA_COFFEE', 1025);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1077, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1025);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1077, '/uploads/menu-items/item_1077.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1077, 1025);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1154, 1077, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1462, 1154, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1463, 1154, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1464, 1154, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1155, 1077, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1465, 1155, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1466, 1155, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1467, 1155, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1078, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1025);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1078, '/uploads/menu-items/item_1078.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1078, 1025);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1156, 1078, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1468, 1156, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1469, 1156, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1470, 1156, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1157, 1078, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1471, 1157, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1472, 1157, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1473, 1157, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1079, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1025);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1079, '/uploads/menu-items/item_1079.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1079, 1025);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1158, 1079, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1474, 1158, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1475, 1158, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1476, 1158, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1159, 1079, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1477, 1159, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1478, 1159, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1479, 1159, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 27: Eatery Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1026, 'Eatery Ville', 'Eatery Ville', 'Eatery Ville', 'مطعم Eatery', 'Avenue de la Liberté, Menzah 7', '+216 26 298 693', 3.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1026.jpg', '/uploads/restaurants/restaurant_1026_icon.jpg', FALSE, FALSE, FALSE, 4.88, '22-48 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1026, 'INDIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1026, 'Indian Dishes', 'Indian Dishes', 'Plats indian', 'أطباق INDIAN', 1026);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1080, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1026);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1080, '/uploads/menu-items/item_1080.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1080, 1026);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1160, 1080, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1480, 1160, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1481, 1160, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1482, 1160, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1161, 1080, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1483, 1161, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1484, 1161, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1485, 1161, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1081, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1026);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1081, '/uploads/menu-items/item_1081.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1081, 1026);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1162, 1081, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1486, 1162, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1487, 1162, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1488, 1162, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1163, 1081, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1489, 1163, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1490, 1163, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1491, 1163, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1082, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1026);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1082, '/uploads/menu-items/item_1082.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1082, 1026);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1164, 1082, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1492, 1164, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1493, 1164, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1494, 1164, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1165, 1082, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1495, 1165, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1496, 1165, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1497, 1165, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 28: Tacos Express Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1027, 'Tacos Express Royal', 'Tacos Express Royal', 'Tacos Express Royal', 'مطعم Tacos Express', 'Avenue Mohamed V, Menzah 5', '+216 25 350 887', 3.6, 'Mexican tacos with fresh ingredients', 'Mexican tacos with fresh ingredients', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1027.jpg', '/uploads/restaurants/restaurant_1027_icon.jpg', FALSE, FALSE, FALSE, 1.73, '23-52 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1027, 'TACOS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1027, 'Tacos Dishes', 'Tacos Dishes', 'Plats tacos', 'أطباق TACOS', 1027);

-- Menu item: Fish Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1083, 'Fish Tacos', 'Fish Tacos', 'Tacos Poisson', 'تاكو بالسمك', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', TRUE, 9.0, TRUE, 1027);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1083, '/uploads/menu-items/item_1083.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1083, 1027);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1166, 1083, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1498, 1166, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1499, 1166, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1500, 1166, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1167, 1083, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1501, 1167, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1502, 1167, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1503, 1167, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Chicken Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1084, 'Chicken Tacos', 'Chicken Tacos', 'Tacos Poulet', 'تاكو بالدجاج', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', FALSE, 7.5, TRUE, 1027);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1084, '/uploads/menu-items/item_1084.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1084, 1027);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1168, 1084, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1504, 1168, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1505, 1168, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1506, 1168, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1169, 1084, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1507, 1169, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1508, 1169, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1509, 1169, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Beef Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1085, 'Beef Tacos', 'Beef Tacos', 'Tacos Boeuf', 'تاكو باللحم', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', FALSE, 8.0, TRUE, 1027);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1085, '/uploads/menu-items/item_1085.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1085, 1027);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1170, 1085, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1510, 1170, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1511, 1170, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1512, 1170, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1171, 1085, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1513, 1171, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1514, 1171, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1515, 1171, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 29: Bella Italia Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1028, 'Bella Italia Premium', 'Bella Italia Premium', 'Bella Italia Premium', 'مطعم Bella Italia', 'Rue des Roses, Menzah 8', '+216 21 597 811', 4.5, 'Traditional Italian dishes prepared with love', 'Traditional Italian dishes prepared with love', 'Plats italiens traditionnels préparés avec amour', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1028.jpg', '/uploads/restaurants/restaurant_1028_icon.jpg', FALSE, TRUE, FALSE, 4.22, '30-56 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1028, 'ITALIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1028, 'Italian Dishes', 'Italian Dishes', 'Plats italian', 'أطباق ITALIAN', 1028);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1086, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1028);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1086, '/uploads/menu-items/item_1086.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1086, 1028);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1172, 1086, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1516, 1172, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1517, 1172, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1518, 1172, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1173, 1086, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1519, 1173, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1520, 1173, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1521, 1173, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1087, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1028);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1087, '/uploads/menu-items/item_1087.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1087, 1028);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1174, 1087, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1522, 1174, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1523, 1174, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1524, 1174, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1175, 1087, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1525, 1175, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1526, 1175, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1527, 1175, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1088, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1028);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1088, '/uploads/menu-items/item_1088.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1088, 1028);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1176, 1088, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1528, 1176, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1529, 1176, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1530, 1176, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1177, 1088, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1531, 1177, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1532, 1177, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1533, 1177, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 30: Bistro Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1029, 'Bistro Express', 'Bistro Express', 'Bistro Express', 'مطعم Bistro', 'Rue des Orangers, Menzah 6', '+216 21 936 182', 4.3, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1029.jpg', '/uploads/restaurants/restaurant_1029_icon.jpg', FALSE, TRUE, FALSE, 2.34, '27-50 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1029, 'SADWICH');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1029, 'Sadwich Dishes', 'Sadwich Dishes', 'Plats sadwich', 'أطباق SADWICH', 1029);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1089, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1029);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1089, '/uploads/menu-items/item_1089.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1089, 1029);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1178, 1089, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1534, 1178, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1535, 1178, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1536, 1178, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1179, 1089, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1537, 1179, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1538, 1179, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1539, 1179, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1090, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1029);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1090, '/uploads/menu-items/item_1090.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1090, 1029);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1180, 1090, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1540, 1180, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1541, 1180, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1542, 1180, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1181, 1090, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1543, 1181, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1544, 1181, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1545, 1181, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1091, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1029);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1091, '/uploads/menu-items/item_1091.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1091, 1029);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1182, 1091, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1546, 1182, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1547, 1182, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1548, 1182, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1183, 1091, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1549, 1183, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1550, 1183, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1551, 1183, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 31: Sea Breeze Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1030, 'Sea Breeze Royal', 'Sea Breeze Royal', 'Sea Breeze Royal', 'مطعم Sea Breeze', 'Rue des Roses, Menzah 8', '+216 21 408 784', 4.6, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1030.jpg', '/uploads/restaurants/restaurant_1030_icon.jpg', TRUE, FALSE, FALSE, 4.98, '18-46 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1030, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1030, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1030);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1092, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1030);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1092, '/uploads/menu-items/item_1092.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1092, 1030);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1184, 1092, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1552, 1184, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1553, 1184, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1554, 1184, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1185, 1092, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1555, 1185, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1556, 1185, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1557, 1185, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1093, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1030);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1093, '/uploads/menu-items/item_1093.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1093, 1030);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1186, 1093, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1558, 1186, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1559, 1186, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1560, 1186, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1187, 1093, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1561, 1187, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1562, 1187, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1563, 1187, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1094, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1030);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1094, '/uploads/menu-items/item_1094.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1094, 1030);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1188, 1094, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1564, 1188, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1565, 1188, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1566, 1188, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1189, 1094, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1567, 1189, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1568, 1189, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1569, 1189, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 32: Bread & Co Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1031, 'Bread & Co Ville', 'Bread & Co Ville', 'Bread & Co Ville', 'مطعم Bread & Co', 'Rue des Orangers, Menzah 6', '+216 28 168 587', 3.5, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1031.jpg', '/uploads/restaurants/restaurant_1031_icon.jpg', FALSE, FALSE, FALSE, 1.15, '20-60 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1031, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1031, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1031);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1095, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1031);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1095, '/uploads/menu-items/item_1095.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1095, 1031);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1190, 1095, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1570, 1190, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1571, 1190, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1572, 1190, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1191, 1095, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1573, 1191, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1574, 1191, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1575, 1191, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1096, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1031);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1096, '/uploads/menu-items/item_1096.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1096, 1031);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1192, 1096, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1576, 1192, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1577, 1192, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1578, 1192, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1193, 1096, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1579, 1193, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1580, 1193, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1581, 1193, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1097, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1031);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1097, '/uploads/menu-items/item_1097.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1097, 1031);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1194, 1097, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1582, 1194, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1583, 1194, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1584, 1194, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1195, 1097, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1585, 1195, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1586, 1195, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1587, 1195, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 33: Café Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1032, 'Café Palace', 'Café Palace', 'Café Palais', 'مطعم Café', 'Avenue de la Liberté, Menzah 7', '+216 24 526 832', 4.7, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1032.jpg', '/uploads/restaurants/restaurant_1032_icon.jpg', TRUE, TRUE, FALSE, 1.88, '18-52 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1032, 'BREAKFAST');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1032, 'Breakfast Dishes', 'Breakfast Dishes', 'Plats breakfast', 'أطباق BREAKFAST', 1032);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1098, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1032);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1098, '/uploads/menu-items/item_1098.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1098, 1032);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1196, 1098, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1588, 1196, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1589, 1196, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1590, 1196, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1197, 1098, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1591, 1197, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1592, 1197, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1593, 1197, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1099, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1032);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1099, '/uploads/menu-items/item_1099.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1099, 1032);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1198, 1099, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1594, 1198, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1595, 1198, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1596, 1198, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1199, 1099, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1597, 1199, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1598, 1199, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1599, 1199, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1100, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1032);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1100, '/uploads/menu-items/item_1100.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1100, 1032);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1200, 1100, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1600, 1200, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1601, 1200, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1602, 1200, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1201, 1100, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1603, 1201, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1604, 1201, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1605, 1201, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 34: Eatery Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1033, 'Eatery Plus', 'Eatery Plus', 'Eatery Plus', 'مطعم Eatery', 'Avenue Habib Bourguiba, Ariana', '+216 22 446 630', 4.3, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1033.jpg', '/uploads/restaurants/restaurant_1033_icon.jpg', FALSE, FALSE, FALSE, 3.73, '25-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1033, 'SADWICH');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1033, 'Sadwich Dishes', 'Sadwich Dishes', 'Plats sadwich', 'أطباق SADWICH', 1033);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1101, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1033);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1101, '/uploads/menu-items/item_1101.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1101, 1033);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1202, 1101, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1606, 1202, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1607, 1202, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1608, 1202, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1203, 1101, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1609, 1203, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1610, 1203, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1611, 1203, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1102, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1033);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1102, '/uploads/menu-items/item_1102.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1102, 1033);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1204, 1102, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1612, 1204, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1613, 1204, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1614, 1204, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1205, 1102, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1615, 1205, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1616, 1205, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1617, 1205, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1103, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1033);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1103, '/uploads/menu-items/item_1103.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1103, 1033);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1206, 1103, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1618, 1206, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1619, 1206, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1620, 1206, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1207, 1103, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1621, 1207, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1622, 1207, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1623, 1207, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 35: Chicken Corner Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1034, 'Chicken Corner Royal', 'Chicken Corner Royal', 'Chicken Coin Royal', 'مطعم Chicken Corner', 'Avenue Mohamed V, Menzah 5', '+216 28 800 962', 3.8, 'Grilled and fried chicken specialties', 'Grilled and fried chicken specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1034.jpg', '/uploads/restaurants/restaurant_1034_icon.jpg', FALSE, FALSE, FALSE, 4.37, '22-44 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1034, 'CHICKEN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1034, 'Chicken Dishes', 'Chicken Dishes', 'Plats chicken', 'أطباق CHICKEN', 1034);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1104, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1034);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1104, '/uploads/menu-items/item_1104.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1104, 1034);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1208, 1104, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1624, 1208, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1625, 1208, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1626, 1208, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1209, 1104, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1627, 1209, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1628, 1209, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1629, 1209, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1105, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1034);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1105, '/uploads/menu-items/item_1105.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1105, 1034);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1210, 1105, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1630, 1210, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1631, 1210, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1632, 1210, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1211, 1105, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1633, 1211, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1634, 1211, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1635, 1211, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1106, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1034);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1106, '/uploads/menu-items/item_1106.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1106, 1034);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1212, 1106, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1636, 1212, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1637, 1212, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1638, 1212, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1213, 1106, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1639, 1213, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1640, 1213, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1641, 1213, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 36: Restaurant Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1035, 'Restaurant Premium', 'Restaurant Premium', 'Restaurant Premium', 'مطعم Restaurant', 'Rue des Roses, Menzah 8', '+216 22 519 378', 4.3, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1035.jpg', '/uploads/restaurants/restaurant_1035_icon.jpg', FALSE, FALSE, TRUE, 2.27, '16-48 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1035, 'MEXICAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1035, 'Mexican Dishes', 'Mexican Dishes', 'Plats mexican', 'أطباق MEXICAN', 1035);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1107, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1035);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1107, '/uploads/menu-items/item_1107.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1107, 1035);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1214, 1107, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1642, 1214, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1643, 1214, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1644, 1214, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1215, 1107, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1645, 1215, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1646, 1215, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1647, 1215, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1108, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1035);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1108, '/uploads/menu-items/item_1108.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1108, 1035);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1216, 1108, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1648, 1216, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1649, 1216, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1650, 1216, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1217, 1108, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1651, 1217, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1652, 1217, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1653, 1217, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1109, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1035);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1109, '/uploads/menu-items/item_1109.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1109, 1035);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1218, 1109, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1654, 1218, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1655, 1218, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1656, 1218, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1219, 1109, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1657, 1219, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1658, 1219, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1659, 1219, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 37: Chicken House Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1036, 'Chicken House Express', 'Chicken House Express', 'Chicken Maison Express', 'مطعم Chicken House', 'Boulevard de l'Environnement, Menzah 9', '+216 20 653 981', 4.8, 'Grilled and fried chicken specialties', 'Grilled and fried chicken specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8167, 10.2, '/uploads/restaurants/restaurant_1036.jpg', '/uploads/restaurants/restaurant_1036_icon.jpg', FALSE, FALSE, FALSE, 3.0, '26-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1036, 'CHICKEN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1036, 'Chicken Dishes', 'Chicken Dishes', 'Plats chicken', 'أطباق CHICKEN', 1036);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1110, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1036);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1110, '/uploads/menu-items/item_1110.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1110, 1036);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1220, 1110, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1660, 1220, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1661, 1220, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1662, 1220, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1221, 1110, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1663, 1221, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1664, 1221, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1665, 1221, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1111, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1036);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1111, '/uploads/menu-items/item_1111.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1111, 1036);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1222, 1111, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1666, 1222, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1667, 1222, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1668, 1222, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1223, 1111, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1669, 1223, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1670, 1223, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1671, 1223, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1112, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1036);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1112, '/uploads/menu-items/item_1112.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1112, 1036);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1224, 1112, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1672, 1224, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1673, 1224, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1674, 1224, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1225, 1112, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1675, 1225, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1676, 1225, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1677, 1225, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 38: Sweet Corner Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1037, 'Sweet Corner Palace', 'Sweet Corner Palace', 'Sweet Coin Palais', 'مطعم Sweet Corner', 'Rue de la République, Ariana', '+216 20 969 313', 5.0, 'Delicious desserts and sweet treats', 'Delicious desserts and sweet treats', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1037.jpg', '/uploads/restaurants/restaurant_1037_icon.jpg', FALSE, FALSE, FALSE, 3.55, '15-41 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1037, 'SWEETS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1037, 'Sweets Dishes', 'Sweets Dishes', 'Plats sweets', 'أطباق SWEETS', 1037);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1113, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1037);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1113, '/uploads/menu-items/item_1113.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1113, 1037);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1226, 1113, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1678, 1226, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1679, 1226, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1680, 1226, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1227, 1113, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1681, 1227, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1682, 1227, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1683, 1227, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1114, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1037);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1114, '/uploads/menu-items/item_1114.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1114, 1037);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1228, 1114, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1684, 1228, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1685, 1228, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1686, 1228, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1229, 1114, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1687, 1229, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1688, 1229, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1689, 1229, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1115, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1037);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1115, '/uploads/menu-items/item_1115.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1115, 1037);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1230, 1115, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1690, 1230, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1691, 1230, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1692, 1230, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1231, 1115, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1693, 1231, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1694, 1231, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1695, 1231, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 39: Tea Garden Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1038, 'Tea Garden Royal', 'Tea Garden Royal', 'Tea Garden Royal', 'مطعم Tea Garden', 'Avenue Habib Bourguiba, Ariana', '+216 24 188 727', 3.8, 'Quality coffee and tea in a cozy atmosphere', 'Quality coffee and tea in a cozy atmosphere', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1038.jpg', '/uploads/restaurants/restaurant_1038_icon.jpg', FALSE, FALSE, FALSE, 3.71, '21-40 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1038, 'TEA_COFFEE');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1038, 'Tea Coffee Dishes', 'Tea Coffee Dishes', 'Plats tea coffee', 'أطباق TEA_COFFEE', 1038);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1116, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1038);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1116, '/uploads/menu-items/item_1116.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1116, 1038);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1232, 1116, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1696, 1232, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1697, 1232, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1698, 1232, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1233, 1116, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1699, 1233, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1700, 1233, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1701, 1233, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1117, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1038);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1117, '/uploads/menu-items/item_1117.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1117, 1038);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1234, 1117, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1702, 1234, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1703, 1234, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1704, 1234, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1235, 1117, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1705, 1235, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1706, 1235, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1707, 1235, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1118, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1038);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1118, '/uploads/menu-items/item_1118.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1118, 1038);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1236, 1118, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1708, 1236, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1709, 1236, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1710, 1236, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1237, 1118, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1711, 1237, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1712, 1237, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1713, 1237, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 40: Sushi Bar Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1039, 'Sushi Bar Ville', 'Sushi Bar Ville', 'Sushi Bar Ville', 'مطعم Sushi Bar', 'Rue des Orangers, Menzah 6', '+216 28 512 439', 4.5, 'Fresh sushi and Japanese specialties', 'Fresh sushi and Japanese specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1039.jpg', '/uploads/restaurants/restaurant_1039_icon.jpg', FALSE, FALSE, FALSE, 1.22, '29-35 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1039, 'SUSHI');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1039, 'Sushi Dishes', 'Sushi Dishes', 'Plats sushi', 'أطباق SUSHI', 1039);

-- Menu item: Mixed Sushi Platter
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1119, 'Mixed Sushi Platter', 'Mixed Sushi Platter', 'Plateau Mixte Sushi', 'طبق سوشي مشكل', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', TRUE, 22.0, TRUE, 1039);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1119, '/uploads/menu-items/item_1119.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1119, 1039);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1238, 1119, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1714, 1238, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1715, 1238, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1716, 1238, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1239, 1119, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1717, 1239, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1718, 1239, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1719, 1239, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: California Roll
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1120, 'California Roll', 'California Roll', 'California Roll', 'كاليفورنيا رول', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', FALSE, 12.0, TRUE, 1039);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1120, '/uploads/menu-items/item_1120.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1120, 1039);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1240, 1120, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1720, 1240, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1721, 1240, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1722, 1240, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1241, 1120, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1723, 1241, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1724, 1241, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1725, 1241, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Tuna Sashimi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1121, 'Tuna Sashimi', 'Tuna Sashimi', 'Sashimi Thon', 'ساشيمي التونة', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', FALSE, 16.0, TRUE, 1039);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1121, '/uploads/menu-items/item_1121.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1121, 1039);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1242, 1121, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1726, 1242, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1727, 1242, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1728, 1242, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1243, 1121, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1729, 1243, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1730, 1243, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1731, 1243, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 41: Napoli Pizza Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1040, 'Napoli Pizza Express', 'Napoli Pizza Express', 'Napoli Pizza Express', 'مطعم Napoli Pizza', 'Boulevard de l'Aéroport, Aouina', '+216 27 849 827', 4.3, 'Authentic wood-fired pizzas with fresh ingredients', 'Authentic wood-fired pizzas with fresh ingredients', 'Pizzas authentiques au feu de bois avec des ingrédients frais', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1040.jpg', '/uploads/restaurants/restaurant_1040_icon.jpg', FALSE, FALSE, FALSE, 3.49, '16-36 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1040, 'PIZZA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1040, 'Pizza Dishes', 'Pizza Dishes', 'Plats pizza', 'أطباق PIZZA', 1040);

-- Menu item: Pepperoni Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1122, 'Pepperoni Pizza', 'Pepperoni Pizza', 'Pizza Pepperoni', 'بيتزا ببروني', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', TRUE, 14.0, TRUE, 1040);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1122, '/uploads/menu-items/item_1122.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1122, 1040);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1244, 1122, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1732, 1244, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1733, 1244, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1734, 1244, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1245, 1122, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1735, 1245, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1736, 1245, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1737, 1245, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Four Cheese Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1123, 'Four Cheese Pizza', 'Four Cheese Pizza', 'Pizza Quatre Fromages', 'بيتزا بأربعة أجبان', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', FALSE, 15.5, TRUE, 1040);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1123, '/uploads/menu-items/item_1123.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1123, 1040);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1246, 1123, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1738, 1246, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1739, 1246, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1740, 1246, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1247, 1123, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1741, 1247, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1742, 1247, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1743, 1247, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Vegetarian Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1124, 'Vegetarian Pizza', 'Vegetarian Pizza', 'Pizza Végétarienne', 'بيتزا نباتية', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', FALSE, 13.0, TRUE, 1040);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1124, '/uploads/menu-items/item_1124.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1124, 1040);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1248, 1124, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1744, 1248, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1745, 1248, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1746, 1248, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1249, 1124, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1747, 1249, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1748, 1249, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1749, 1249, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Margherita Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1125, 'Margherita Pizza', 'Margherita Pizza', 'Pizza Margherita', 'بيتزا مارغريتا', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', FALSE, 12.5, TRUE, 1040);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1125, '/uploads/menu-items/item_1125.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1125, 1040);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1250, 1125, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1750, 1250, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1751, 1250, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1752, 1250, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1251, 1125, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1753, 1251, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1754, 1251, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1755, 1251, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 42: Eatery Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1041, 'Eatery Centre', 'Eatery Centre', 'Eatery Centre', 'مطعم Eatery', 'Boulevard de l'Aéroport, Aouina', '+216 26 587 632', 3.5, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1041.jpg', '/uploads/restaurants/restaurant_1041_icon.jpg', FALSE, FALSE, FALSE, 2.18, '28-40 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1041, 'TRADITIONAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1041, 'Traditional Dishes', 'Traditional Dishes', 'Plats traditional', 'أطباق TRADITIONAL', 1041);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1126, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1041);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1126, '/uploads/menu-items/item_1126.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1126, 1041);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1252, 1126, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1756, 1252, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1757, 1252, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1758, 1252, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1253, 1126, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1759, 1253, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1760, 1253, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1761, 1253, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1127, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1041);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1127, '/uploads/menu-items/item_1127.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1127, 1041);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1254, 1127, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1762, 1254, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1763, 1254, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1764, 1254, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1255, 1127, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1765, 1255, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1766, 1255, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1767, 1255, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1128, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1041);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1128, '/uploads/menu-items/item_1128.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1128, 1041);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1256, 1128, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1768, 1256, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1769, 1256, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1770, 1256, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1257, 1128, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1771, 1257, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1772, 1257, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1773, 1257, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 43: Eatery Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1042, 'Eatery Royal', 'Eatery Royal', 'Eatery Royal', 'مطعم Eatery', 'Rue des Jasmins, Ariana Riadh', '+216 26 131 499', 5.0, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8419, 10.1958, '/uploads/restaurants/restaurant_1042.jpg', '/uploads/restaurants/restaurant_1042_icon.jpg', FALSE, FALSE, FALSE, 2.54, '19-59 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1042, 'SALDAS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1042, 'Saldas Dishes', 'Saldas Dishes', 'Plats saldas', 'أطباق SALDAS', 1042);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1129, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1042);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1129, '/uploads/menu-items/item_1129.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1129, 1042);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1258, 1129, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1774, 1258, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1775, 1258, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1776, 1258, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1259, 1129, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1777, 1259, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1778, 1259, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1779, 1259, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1130, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1042);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1130, '/uploads/menu-items/item_1130.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1130, 1042);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1260, 1130, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1780, 1260, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1781, 1260, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1782, 1260, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1261, 1130, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1783, 1261, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1784, 1261, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1785, 1261, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1131, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1042);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1131, '/uploads/menu-items/item_1131.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1131, 1042);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1262, 1131, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1786, 1262, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1787, 1262, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1788, 1262, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1263, 1131, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1789, 1263, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1790, 1263, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1791, 1263, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 44: Pasta Corner Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1043, 'Pasta Corner Centre', 'Pasta Corner Centre', 'Pasta Coin Centre', 'مطعم Pasta Corner', 'Rue des Jasmins, Ariana Riadh', '+216 28 271 613', 3.7, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8419, 10.1958, '/uploads/restaurants/restaurant_1043.jpg', '/uploads/restaurants/restaurant_1043_icon.jpg', FALSE, FALSE, FALSE, 3.04, '17-55 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1043, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1043, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1043);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1132, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1043);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1132, '/uploads/menu-items/item_1132.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1132, 1043);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1264, 1132, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1792, 1264, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1793, 1264, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1794, 1264, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1265, 1132, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1795, 1265, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1796, 1265, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1797, 1265, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1133, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1043);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1133, '/uploads/menu-items/item_1133.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1133, 1043);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1266, 1133, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1798, 1266, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1799, 1266, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1800, 1266, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1267, 1133, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1801, 1267, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1802, 1267, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1803, 1267, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1134, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1043);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1134, '/uploads/menu-items/item_1134.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1134, 1043);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1268, 1134, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1804, 1268, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1805, 1268, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1806, 1268, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1269, 1134, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1807, 1269, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1808, 1269, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1809, 1269, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 45: Candy Shop Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1044, 'Candy Shop Palace', 'Candy Shop Palace', 'Candy Shop Palais', 'مطعم Candy Shop', 'Avenue de Carthage, Aouina', '+216 23 749 586', 3.7, 'Delicious desserts and sweet treats', 'Delicious desserts and sweet treats', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1044.jpg', '/uploads/restaurants/restaurant_1044_icon.jpg', FALSE, FALSE, FALSE, 3.27, '28-42 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1044, 'SWEETS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1044, 'Sweets Dishes', 'Sweets Dishes', 'Plats sweets', 'أطباق SWEETS', 1044);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1135, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1044);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1135, '/uploads/menu-items/item_1135.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1135, 1044);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1270, 1135, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1810, 1270, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1811, 1270, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1812, 1270, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1271, 1135, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1813, 1271, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1814, 1271, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1815, 1271, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1136, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1044);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1136, '/uploads/menu-items/item_1136.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1136, 1044);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1272, 1136, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1816, 1272, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1817, 1272, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1818, 1272, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1273, 1136, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1819, 1273, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1820, 1273, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1821, 1273, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1137, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1044);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1137, '/uploads/menu-items/item_1137.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1137, 1044);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1274, 1137, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1822, 1274, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1823, 1274, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1824, 1274, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1275, 1137, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1825, 1275, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1826, 1275, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1827, 1275, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 46: Chicken Express Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1045, 'Chicken Express Centre', 'Chicken Express Centre', 'Chicken Express Centre', 'مطعم Chicken Express', 'Rue des Roses, Menzah 8', '+216 24 740 222', 4.1, 'Grilled and fried chicken specialties', 'Grilled and fried chicken specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1045.jpg', '/uploads/restaurants/restaurant_1045_icon.jpg', FALSE, FALSE, FALSE, 1.01, '27-35 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1045, 'CHICKEN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1045, 'Chicken Dishes', 'Chicken Dishes', 'Plats chicken', 'أطباق CHICKEN', 1045);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1138, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1045);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1138, '/uploads/menu-items/item_1138.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1138, 1045);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1276, 1138, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1828, 1276, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1829, 1276, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1830, 1276, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1277, 1138, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1831, 1277, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1832, 1277, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1833, 1277, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1139, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1045);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1139, '/uploads/menu-items/item_1139.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1139, 1045);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1278, 1139, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1834, 1278, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1835, 1278, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1836, 1278, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1279, 1139, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1837, 1279, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1838, 1279, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1839, 1279, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1140, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1045);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1140, '/uploads/menu-items/item_1140.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1140, 1045);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1280, 1140, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1840, 1280, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1841, 1280, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1842, 1280, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1281, 1140, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1843, 1281, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1844, 1281, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1845, 1281, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 47: Pizza Roma Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1046, 'Pizza Roma Premium', 'Pizza Roma Premium', 'Pizza Roma Premium', 'مطعم Pizza Roma', 'Avenue de la Liberté, Menzah 7', '+216 23 802 364', 3.9, 'Authentic wood-fired pizzas with fresh ingredients', 'Authentic wood-fired pizzas with fresh ingredients', 'Pizzas authentiques au feu de bois avec des ingrédients frais', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1046.jpg', '/uploads/restaurants/restaurant_1046_icon.jpg', FALSE, FALSE, FALSE, 2.8, '29-46 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1046, 'PIZZA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1046, 'Pizza Dishes', 'Pizza Dishes', 'Plats pizza', 'أطباق PIZZA', 1046);

-- Menu item: Pepperoni Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1141, 'Pepperoni Pizza', 'Pepperoni Pizza', 'Pizza Pepperoni', 'بيتزا ببروني', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', TRUE, 14.0, TRUE, 1046);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1141, '/uploads/menu-items/item_1141.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1141, 1046);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1282, 1141, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1846, 1282, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1847, 1282, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1848, 1282, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1283, 1141, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1849, 1283, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1850, 1283, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1851, 1283, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Vegetarian Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1142, 'Vegetarian Pizza', 'Vegetarian Pizza', 'Pizza Végétarienne', 'بيتزا نباتية', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', FALSE, 13.0, TRUE, 1046);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1142, '/uploads/menu-items/item_1142.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1142, 1046);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1284, 1142, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1852, 1284, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1853, 1284, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1854, 1284, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1285, 1142, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1855, 1285, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1856, 1285, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1857, 1285, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Four Cheese Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1143, 'Four Cheese Pizza', 'Four Cheese Pizza', 'Pizza Quatre Fromages', 'بيتزا بأربعة أجبان', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', 'Mozzarella, cheddar, parmesan, gorgonzola', FALSE, 15.5, TRUE, 1046);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1143, '/uploads/menu-items/item_1143.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1143, 1046);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1286, 1143, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1858, 1286, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1859, 1286, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1860, 1286, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1287, 1143, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1861, 1287, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1862, 1287, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1863, 1287, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Margherita Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1144, 'Margherita Pizza', 'Margherita Pizza', 'Pizza Margherita', 'بيتزا مارغريتا', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', FALSE, 12.5, TRUE, 1046);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1144, '/uploads/menu-items/item_1144.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1144, 1046);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1288, 1144, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1864, 1288, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1865, 1288, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1866, 1288, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1289, 1144, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1867, 1289, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1868, 1289, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1869, 1289, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 48: Quick Eat Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1047, 'Quick Eat Express', 'Quick Eat Express', 'Quick Eat Express', 'مطعم Quick Eat', 'Avenue Habib Bourguiba, Ariana', '+216 29 198 283', 4.7, 'Quick and tasty meals for people on the go', 'Quick and tasty meals for people on the go', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1047.jpg', '/uploads/restaurants/restaurant_1047_icon.jpg', FALSE, FALSE, FALSE, 2.62, '30-59 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1047, 'FAST_FOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1047, 'Fast Food Dishes', 'Fast Food Dishes', 'Plats fast food', 'أطباق FAST_FOOD', 1047);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1145, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1047);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1145, '/uploads/menu-items/item_1145.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1145, 1047);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1290, 1145, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1870, 1290, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1871, 1290, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1872, 1290, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1291, 1145, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1873, 1291, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1874, 1291, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1875, 1291, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1146, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1047);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1146, '/uploads/menu-items/item_1146.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1146, 1047);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1292, 1146, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1876, 1292, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1877, 1292, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1878, 1292, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1293, 1146, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1879, 1293, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1880, 1293, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1881, 1293, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1147, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1047);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1147, '/uploads/menu-items/item_1147.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1147, 1047);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1294, 1147, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1882, 1294, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1883, 1294, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1884, 1294, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1295, 1147, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1885, 1295, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1886, 1295, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1887, 1295, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 49: BBQ Grill Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1048, 'BBQ Grill Premium', 'BBQ Grill Premium', 'BBQ Grill Premium', 'مطعم BBQ Grill', 'Boulevard de l'Aéroport, Aouina', '+216 28 846 971', 4.4, 'Grilled meats and BBQ specialties', 'Grilled meats and BBQ specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1048.jpg', '/uploads/restaurants/restaurant_1048_icon.jpg', FALSE, FALSE, FALSE, 3.93, '24-55 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1048, 'GRILL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1048, 'Grill Dishes', 'Grill Dishes', 'Plats grill', 'أطباق GRILL', 1048);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1148, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1048);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1148, '/uploads/menu-items/item_1148.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1148, 1048);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1296, 1148, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1888, 1296, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1889, 1296, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1890, 1296, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1297, 1148, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1891, 1297, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1892, 1297, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1893, 1297, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1149, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1048);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1149, '/uploads/menu-items/item_1149.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1149, 1048);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1298, 1149, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1894, 1298, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1895, 1298, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1896, 1298, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1299, 1149, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1897, 1299, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1898, 1299, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1899, 1299, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1150, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1048);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1150, '/uploads/menu-items/item_1150.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1150, 1048);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1300, 1150, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1900, 1300, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1901, 1300, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1902, 1300, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1301, 1150, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1903, 1301, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1904, 1301, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1905, 1301, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 50: French Bakery Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1049, 'French Bakery Centre', 'French Bakery Centre', 'French Bakery Centre', 'مطعم French Bakery', 'Avenue de la Liberté, Ariana Essoughra', '+216 23 728 146', 3.5, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1049.jpg', '/uploads/restaurants/restaurant_1049_icon.jpg', FALSE, FALSE, FALSE, 4.63, '25-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1049, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1049, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1049);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1151, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1049);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1151, '/uploads/menu-items/item_1151.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1151, 1049);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1302, 1151, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1906, 1302, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1907, 1302, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1908, 1302, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1303, 1151, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1909, 1303, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1910, 1303, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1911, 1303, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1152, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1049);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1152, '/uploads/menu-items/item_1152.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1152, 1049);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1304, 1152, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1912, 1304, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1913, 1304, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1914, 1304, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1305, 1152, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1915, 1305, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1916, 1305, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1917, 1305, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1153, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1049);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1153, '/uploads/menu-items/item_1153.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1153, 1049);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1306, 1153, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1918, 1306, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1919, 1306, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1920, 1306, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1307, 1153, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1921, 1307, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1922, 1307, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1923, 1307, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 51: Restaurant Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1050, 'Restaurant Palace', 'Restaurant Palace', 'Restaurant Palais', 'مطعم Restaurant', 'Rue des Orangers, Menzah 6', '+216 22 889 652', 4.1, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1050.jpg', '/uploads/restaurants/restaurant_1050_icon.jpg', FALSE, FALSE, FALSE, 1.11, '20-53 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1050, 'SNACKS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1050, 'Snacks Dishes', 'Snacks Dishes', 'Plats snacks', 'أطباق SNACKS', 1050);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1154, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1050);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1154, '/uploads/menu-items/item_1154.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1154, 1050);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1308, 1154, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1924, 1308, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1925, 1308, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1926, 1308, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1309, 1154, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1927, 1309, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1928, 1309, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1929, 1309, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1155, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1050);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1155, '/uploads/menu-items/item_1155.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1155, 1050);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1310, 1155, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1930, 1310, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1931, 1310, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1932, 1310, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1311, 1155, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1933, 1311, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1934, 1311, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1935, 1311, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1156, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1050);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1156, '/uploads/menu-items/item_1156.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1156, 1050);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1312, 1156, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1936, 1312, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1937, 1312, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1938, 1312, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1313, 1156, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1939, 1313, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1940, 1313, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1941, 1313, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 52: Pasta Corner Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1051, 'Pasta Corner Palace', 'Pasta Corner Palace', 'Pasta Coin Palais', 'مطعم Pasta Corner', 'Rue de la République, Ariana', '+216 22 742 363', 4.0, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1051.jpg', '/uploads/restaurants/restaurant_1051_icon.jpg', FALSE, TRUE, FALSE, 1.42, '30-50 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1051, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1051, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1051);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1157, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1051);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1157, '/uploads/menu-items/item_1157.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1157, 1051);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1314, 1157, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1942, 1314, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1943, 1314, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1944, 1314, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1315, 1157, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1945, 1315, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1946, 1315, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1947, 1315, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1158, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1051);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1158, '/uploads/menu-items/item_1158.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1158, 1051);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1316, 1158, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1948, 1316, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1949, 1316, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1950, 1316, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1317, 1158, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1951, 1317, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1952, 1317, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1953, 1317, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1159, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1051);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1159, '/uploads/menu-items/item_1159.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1159, 1051);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1318, 1159, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1954, 1318, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1955, 1318, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1956, 1318, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1319, 1159, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1957, 1319, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1958, 1319, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1959, 1319, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 53: La Boulangerie Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1052, 'La Boulangerie Royal', 'La Boulangerie Royal', 'La Boulangerie Royal', 'مطعم La Boulangerie', 'Boulevard de l'Aéroport, Aouina', '+216 29 289 965', 3.7, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1052.jpg', '/uploads/restaurants/restaurant_1052_icon.jpg', FALSE, FALSE, FALSE, 3.0, '20-57 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1052, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1052, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1052);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1160, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1052);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1160, '/uploads/menu-items/item_1160.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1160, 1052);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1320, 1160, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1960, 1320, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1961, 1320, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1962, 1320, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1321, 1160, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1963, 1321, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1964, 1321, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1965, 1321, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1161, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1052);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1161, '/uploads/menu-items/item_1161.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1161, 1052);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1322, 1161, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1966, 1322, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1967, 1322, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1968, 1322, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1323, 1161, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1969, 1323, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1970, 1323, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1971, 1323, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1162, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1052);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1162, '/uploads/menu-items/item_1162.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1162, 1052);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1324, 1162, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1972, 1324, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1973, 1324, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1974, 1324, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1325, 1162, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1975, 1325, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1976, 1325, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1977, 1325, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 54: Eatery Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1053, 'Eatery Premium', 'Eatery Premium', 'Eatery Premium', 'مطعم Eatery', 'Boulevard de l'Environnement, Menzah 9', '+216 21 951 425', 5.0, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8167, 10.2, '/uploads/restaurants/restaurant_1053.jpg', '/uploads/restaurants/restaurant_1053_icon.jpg', FALSE, FALSE, FALSE, 3.03, '20-43 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1053, 'ORIENTAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1053, 'Oriental Dishes', 'Oriental Dishes', 'Plats oriental', 'أطباق ORIENTAL', 1053);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1163, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1053);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1163, '/uploads/menu-items/item_1163.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1163, 1053);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1326, 1163, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1978, 1326, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1979, 1326, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1980, 1326, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1327, 1163, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1981, 1327, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1982, 1327, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1983, 1327, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1164, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1053);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1164, '/uploads/menu-items/item_1164.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1164, 1053);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1328, 1164, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1984, 1328, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1985, 1328, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1986, 1328, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1329, 1164, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1987, 1329, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1988, 1329, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1989, 1329, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1165, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1053);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1165, '/uploads/menu-items/item_1165.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1165, 1053);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1330, 1165, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1990, 1330, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1991, 1330, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1992, 1330, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1331, 1165, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1993, 1331, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1994, 1331, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1995, 1331, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 55: Wok House Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1054, 'Wok House Plus', 'Wok House Plus', 'Wok Maison Plus', 'مطعم Wok House', 'Rue des Orangers, Menzah 6', '+216 20 772 581', 3.7, 'Delicious Asian cuisine with authentic flavors', 'Delicious Asian cuisine with authentic flavors', 'Délicieuse cuisine asiatique aux saveurs authentiques', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1054.jpg', '/uploads/restaurants/restaurant_1054_icon.jpg', FALSE, TRUE, FALSE, 4.14, '15-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1054, 'ASIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1054, 'Asian Dishes', 'Asian Dishes', 'Plats asian', 'أطباق ASIAN', 1054);

-- Menu item: Spring Rolls
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1166, 'Spring Rolls', 'Spring Rolls', 'Rouleaux de Printemps', 'لفائف الربيع', 'Crispy vegetable rolls', 'Crispy vegetable rolls', 'Crispy vegetable rolls', 'Crispy vegetable rolls', TRUE, 6.5, TRUE, 1054);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1166, '/uploads/menu-items/item_1166.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1166, 1054);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1332, 1166, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1996, 1332, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1997, 1332, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1998, 1332, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1333, 1166, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(1999, 1333, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2000, 1333, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2001, 1333, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Sweet & Sour Chicken
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1167, 'Sweet & Sour Chicken', 'Sweet & Sour Chicken', 'Poulet Aigre-Doux', 'دجاج حلو وحامض', 'Chicken in sweet and sour sauce', 'Chicken in sweet and sour sauce', 'Chicken in sweet and sour sauce', 'Chicken in sweet and sour sauce', FALSE, 11.0, TRUE, 1054);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1167, '/uploads/menu-items/item_1167.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1167, 1054);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1334, 1167, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2002, 1334, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2003, 1334, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2004, 1334, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1335, 1167, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2005, 1335, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2006, 1335, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2007, 1335, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Fried Rice
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1168, 'Fried Rice', 'Fried Rice', 'Riz Frit', 'أرز مقلي', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', FALSE, 8.5, TRUE, 1054);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1168, '/uploads/menu-items/item_1168.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1168, 1054);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1336, 1168, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2008, 1336, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2009, 1336, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2010, 1336, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1337, 1168, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2011, 1337, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2012, 1337, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2013, 1337, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 56: Bistro Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1055, 'Bistro Plus', 'Bistro Plus', 'Bistro Plus', 'مطعم Bistro', 'Avenue Mohamed V, Menzah 5', '+216 22 718 905', 3.9, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1055.jpg', '/uploads/restaurants/restaurant_1055_icon.jpg', FALSE, FALSE, FALSE, 3.29, '23-41 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1055, 'TRADITIONAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1055, 'Traditional Dishes', 'Traditional Dishes', 'Plats traditional', 'أطباق TRADITIONAL', 1055);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1169, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1055);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1169, '/uploads/menu-items/item_1169.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1169, 1055);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1338, 1169, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2014, 1338, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2015, 1338, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2016, 1338, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1339, 1169, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2017, 1339, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2018, 1339, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2019, 1339, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1170, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1055);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1170, '/uploads/menu-items/item_1170.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1170, 1055);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1340, 1170, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2020, 1340, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2021, 1340, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2022, 1340, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1341, 1170, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2023, 1341, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2024, 1341, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2025, 1341, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1171, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1055);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1171, '/uploads/menu-items/item_1171.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1171, 1055);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1342, 1171, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2026, 1342, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2027, 1342, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2028, 1342, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1343, 1171, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2029, 1343, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2030, 1343, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2031, 1343, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 57: Fish Market Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1056, 'Fish Market Centre', 'Fish Market Centre', 'Fish Market Centre', 'مطعم Fish Market', 'Rue de la République, Ariana', '+216 26 518 625', 4.6, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1056.jpg', '/uploads/restaurants/restaurant_1056_icon.jpg', FALSE, FALSE, FALSE, 1.15, '30-40 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1056, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1056, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1056);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1172, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1056);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1172, '/uploads/menu-items/item_1172.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1172, 1056);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1344, 1172, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2032, 1344, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2033, 1344, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2034, 1344, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1345, 1172, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2035, 1345, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2036, 1345, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2037, 1345, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1173, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1056);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1173, '/uploads/menu-items/item_1173.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1173, 1056);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1346, 1173, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2038, 1346, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2039, 1346, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2040, 1346, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1347, 1173, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2041, 1347, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2042, 1347, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2043, 1347, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1174, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1056);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1174, '/uploads/menu-items/item_1174.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1174, 1056);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1348, 1174, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2044, 1348, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2045, 1348, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2046, 1348, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1349, 1174, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2047, 1349, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2048, 1349, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2049, 1349, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 58: Zen Sushi Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1057, 'Zen Sushi Ville', 'Zen Sushi Ville', 'Zen Sushi Ville', 'مطعم Zen Sushi', 'Boulevard de l'Environnement, Menzah 9', '+216 24 871 744', 3.9, 'Fresh sushi and Japanese specialties', 'Fresh sushi and Japanese specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8167, 10.2, '/uploads/restaurants/restaurant_1057.jpg', '/uploads/restaurants/restaurant_1057_icon.jpg', TRUE, FALSE, FALSE, 2.11, '27-36 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1057, 'SUSHI');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1057, 'Sushi Dishes', 'Sushi Dishes', 'Plats sushi', 'أطباق SUSHI', 1057);

-- Menu item: Tuna Sashimi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1175, 'Tuna Sashimi', 'Tuna Sashimi', 'Sashimi Thon', 'ساشيمي التونة', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', TRUE, 16.0, TRUE, 1057);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1175, '/uploads/menu-items/item_1175.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1175, 1057);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1350, 1175, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2050, 1350, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2051, 1350, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2052, 1350, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1351, 1175, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2053, 1351, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2054, 1351, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2055, 1351, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Mixed Sushi Platter
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1176, 'Mixed Sushi Platter', 'Mixed Sushi Platter', 'Plateau Mixte Sushi', 'طبق سوشي مشكل', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', FALSE, 22.0, TRUE, 1057);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1176, '/uploads/menu-items/item_1176.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1176, 1057);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1352, 1176, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2056, 1352, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2057, 1352, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2058, 1352, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1353, 1176, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2059, 1353, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2060, 1353, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2061, 1353, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Salmon Sushi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1177, 'Salmon Sushi', 'Salmon Sushi', 'Sushi Saumon', 'سوشي السلمون', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', FALSE, 14.0, TRUE, 1057);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1177, '/uploads/menu-items/item_1177.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1177, 1057);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1354, 1177, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2062, 1354, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2063, 1354, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2064, 1354, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1355, 1177, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2065, 1355, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2066, 1355, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2067, 1355, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 59: Ocean Catch Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1058, 'Ocean Catch Ville', 'Ocean Catch Ville', 'Ocean Catch Ville', 'مطعم Ocean Catch', 'Avenue de la Liberté, Menzah 7', '+216 22 130 312', 4.8, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1058.jpg', '/uploads/restaurants/restaurant_1058_icon.jpg', FALSE, FALSE, FALSE, 2.12, '26-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1058, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1058, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1058);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1178, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1058);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1178, '/uploads/menu-items/item_1178.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1178, 1058);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1356, 1178, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2068, 1356, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2069, 1356, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2070, 1356, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1357, 1178, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2071, 1357, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2072, 1357, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2073, 1357, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1179, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1058);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1179, '/uploads/menu-items/item_1179.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1179, 1058);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1358, 1179, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2074, 1358, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2075, 1358, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2076, 1358, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1359, 1179, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2077, 1359, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2078, 1359, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2079, 1359, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1180, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1058);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1180, '/uploads/menu-items/item_1180.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1180, 1058);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1360, 1180, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2080, 1360, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2081, 1360, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2082, 1360, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1361, 1180, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2083, 1361, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2084, 1361, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2085, 1361, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 60: Coffee House Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1059, 'Coffee House Plus', 'Coffee House Plus', 'Coffee Maison Plus', 'مطعم Coffee House', 'Rue des Roses, Menzah 8', '+216 24 408 983', 3.8, 'Quality coffee and tea in a cozy atmosphere', 'Quality coffee and tea in a cozy atmosphere', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1059.jpg', '/uploads/restaurants/restaurant_1059_icon.jpg', FALSE, FALSE, FALSE, 3.74, '22-47 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1059, 'TEA_COFFEE');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1059, 'Tea Coffee Dishes', 'Tea Coffee Dishes', 'Plats tea coffee', 'أطباق TEA_COFFEE', 1059);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1181, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1059);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1181, '/uploads/menu-items/item_1181.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1181, 1059);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1362, 1181, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2086, 1362, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2087, 1362, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2088, 1362, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1363, 1181, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2089, 1363, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2090, 1363, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2091, 1363, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1182, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1059);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1182, '/uploads/menu-items/item_1182.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1182, 1059);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1364, 1182, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2092, 1364, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2093, 1364, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2094, 1364, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1365, 1182, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2095, 1365, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2096, 1365, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2097, 1365, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1183, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1059);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1183, '/uploads/menu-items/item_1183.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1183, 1059);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1366, 1183, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2098, 1366, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2099, 1366, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2100, 1366, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1367, 1183, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2101, 1367, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2102, 1367, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2103, 1367, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 61: Ristorante Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1060, 'Ristorante Ville', 'Ristorante Ville', 'Ristorante Ville', 'مطعم Ristorante', 'Avenue de la Liberté, Ariana Essoughra', '+216 22 874 846', 4.8, 'Traditional Italian dishes prepared with love', 'Traditional Italian dishes prepared with love', 'Plats italiens traditionnels préparés avec amour', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1060.jpg', '/uploads/restaurants/restaurant_1060_icon.jpg', FALSE, FALSE, FALSE, 3.37, '28-48 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1060, 'ITALIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1060, 'Italian Dishes', 'Italian Dishes', 'Plats italian', 'أطباق ITALIAN', 1060);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1184, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1060);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1184, '/uploads/menu-items/item_1184.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1184, 1060);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1368, 1184, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2104, 1368, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2105, 1368, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2106, 1368, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1369, 1184, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2107, 1369, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2108, 1369, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2109, 1369, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1185, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1060);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1185, '/uploads/menu-items/item_1185.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1185, 1060);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1370, 1185, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2110, 1370, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2111, 1370, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2112, 1370, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1371, 1185, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2113, 1371, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2114, 1371, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2115, 1371, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1186, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1060);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1186, '/uploads/menu-items/item_1186.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1186, 1060);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1372, 1186, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2116, 1372, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2117, 1372, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2118, 1372, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1373, 1186, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2119, 1373, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2120, 1373, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2121, 1373, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 62: Restaurant Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1061, 'Restaurant Plus', 'Restaurant Plus', 'Restaurant Plus', 'مطعم Restaurant', 'Rue des Orangers, Menzah 6', '+216 24 466 821', 4.5, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1061.jpg', '/uploads/restaurants/restaurant_1061_icon.jpg', FALSE, FALSE, FALSE, 3.83, '28-59 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1061, 'SALDAS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1061, 'Saldas Dishes', 'Saldas Dishes', 'Plats saldas', 'أطباق SALDAS', 1061);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1187, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1061);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1187, '/uploads/menu-items/item_1187.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1187, 1061);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1374, 1187, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2122, 1374, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2123, 1374, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2124, 1374, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1375, 1187, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2125, 1375, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2126, 1375, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2127, 1375, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1188, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1061);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1188, '/uploads/menu-items/item_1188.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1188, 1061);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1376, 1188, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2128, 1376, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2129, 1376, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2130, 1376, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1377, 1188, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2131, 1377, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2132, 1377, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2133, 1377, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1189, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1061);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1189, '/uploads/menu-items/item_1189.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1189, 1061);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1378, 1189, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2134, 1378, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2135, 1378, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2136, 1378, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1379, 1189, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2137, 1379, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2138, 1379, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2139, 1379, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 63: Bistro Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1062, 'Bistro Royal', 'Bistro Royal', 'Bistro Royal', 'مطعم Bistro', 'Rue des Orangers, Menzah 6', '+216 25 566 559', 4.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1062.jpg', '/uploads/restaurants/restaurant_1062_icon.jpg', FALSE, FALSE, FALSE, 1.67, '20-57 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1062, 'INDIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1062, 'Indian Dishes', 'Indian Dishes', 'Plats indian', 'أطباق INDIAN', 1062);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1190, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1062);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1190, '/uploads/menu-items/item_1190.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1190, 1062);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1380, 1190, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2140, 1380, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2141, 1380, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2142, 1380, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1381, 1190, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2143, 1381, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2144, 1381, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2145, 1381, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1191, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1062);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1191, '/uploads/menu-items/item_1191.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1191, 1062);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1382, 1191, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2146, 1382, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2147, 1382, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2148, 1382, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1383, 1191, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2149, 1383, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2150, 1383, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2151, 1383, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1192, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1062);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1192, '/uploads/menu-items/item_1192.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1192, 1062);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1384, 1192, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2152, 1384, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2153, 1384, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2154, 1384, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1385, 1192, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2155, 1385, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2156, 1385, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2157, 1385, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 64: La Pasta Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1063, 'La Pasta Plus', 'La Pasta Plus', 'La Pasta Plus', 'مطعم La Pasta', 'Avenue de la Liberté, Menzah 7', '+216 25 765 717', 4.9, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1063.jpg', '/uploads/restaurants/restaurant_1063_icon.jpg', FALSE, FALSE, FALSE, 1.72, '24-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1063, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1063, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1063);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1193, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1063);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1193, '/uploads/menu-items/item_1193.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1193, 1063);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1386, 1193, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2158, 1386, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2159, 1386, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2160, 1386, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1387, 1193, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2161, 1387, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2162, 1387, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2163, 1387, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1194, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1063);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1194, '/uploads/menu-items/item_1194.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1194, 1063);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1388, 1194, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2164, 1388, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2165, 1388, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2166, 1388, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1389, 1194, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2167, 1389, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2168, 1389, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2169, 1389, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1195, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1063);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1195, '/uploads/menu-items/item_1195.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1195, 1063);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1390, 1195, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2170, 1390, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2171, 1390, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2172, 1390, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1391, 1195, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2173, 1391, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2174, 1391, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2175, 1391, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 65: Tokyo Sushi Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1064, 'Tokyo Sushi Royal', 'Tokyo Sushi Royal', 'Tokyo Sushi Royal', 'مطعم Tokyo Sushi', 'Rue des Orangers, Menzah 6', '+216 24 715 598', 3.5, 'Fresh sushi and Japanese specialties', 'Fresh sushi and Japanese specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1064.jpg', '/uploads/restaurants/restaurant_1064_icon.jpg', FALSE, FALSE, FALSE, 4.3, '17-39 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1064, 'SUSHI');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1064, 'Sushi Dishes', 'Sushi Dishes', 'Plats sushi', 'أطباق SUSHI', 1064);

-- Menu item: Salmon Sushi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1196, 'Salmon Sushi', 'Salmon Sushi', 'Sushi Saumon', 'سوشي السلمون', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', TRUE, 14.0, TRUE, 1064);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1196, '/uploads/menu-items/item_1196.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1196, 1064);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1392, 1196, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2176, 1392, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2177, 1392, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2178, 1392, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1393, 1196, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2179, 1393, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2180, 1393, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2181, 1393, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Mixed Sushi Platter
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1197, 'Mixed Sushi Platter', 'Mixed Sushi Platter', 'Plateau Mixte Sushi', 'طبق سوشي مشكل', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', FALSE, 22.0, TRUE, 1064);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1197, '/uploads/menu-items/item_1197.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1197, 1064);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1394, 1197, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2182, 1394, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2183, 1394, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2184, 1394, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1395, 1197, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2185, 1395, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2186, 1395, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2187, 1395, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: California Roll
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1198, 'California Roll', 'California Roll', 'California Roll', 'كاليفورنيا رول', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', FALSE, 12.0, TRUE, 1064);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1198, '/uploads/menu-items/item_1198.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1198, 1064);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1396, 1198, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2188, 1396, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2189, 1396, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2190, 1396, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1397, 1198, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2191, 1397, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2192, 1397, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2193, 1397, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Tuna Sashimi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1199, 'Tuna Sashimi', 'Tuna Sashimi', 'Sashimi Thon', 'ساشيمي التونة', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', FALSE, 16.0, TRUE, 1064);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1199, '/uploads/menu-items/item_1199.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1199, 1064);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1398, 1199, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2194, 1398, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2195, 1398, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2196, 1398, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1399, 1199, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2197, 1399, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2198, 1399, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2199, 1399, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 66: Bistro Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1065, 'Bistro Royal', 'Bistro Royal', 'Bistro Royal', 'مطعم Bistro', 'Avenue de Carthage, Aouina', '+216 20 505 723', 4.0, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1065.jpg', '/uploads/restaurants/restaurant_1065_icon.jpg', FALSE, FALSE, FALSE, 2.74, '20-37 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1065, 'ORIENTAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1065, 'Oriental Dishes', 'Oriental Dishes', 'Plats oriental', 'أطباق ORIENTAL', 1065);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1200, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1065);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1200, '/uploads/menu-items/item_1200.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1200, 1065);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1400, 1200, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2200, 1400, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2201, 1400, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2202, 1400, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1401, 1200, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2203, 1401, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2204, 1401, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2205, 1401, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1201, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1065);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1201, '/uploads/menu-items/item_1201.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1201, 1065);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1402, 1201, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2206, 1402, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2207, 1402, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2208, 1402, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1403, 1201, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2209, 1403, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2210, 1403, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2211, 1403, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1202, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1065);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1202, '/uploads/menu-items/item_1202.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1202, 1065);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1404, 1202, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2212, 1404, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2213, 1404, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2214, 1404, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1405, 1202, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2215, 1405, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2216, 1405, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2217, 1405, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 67: Pizza Palace Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1066, 'Pizza Palace Centre', 'Pizza Palace Centre', 'Pizza Palais Centre', 'مطعم Pizza Palace', 'Avenue de Carthage, Aouina', '+216 21 914 424', 4.0, 'Authentic wood-fired pizzas with fresh ingredients', 'Authentic wood-fired pizzas with fresh ingredients', 'Pizzas authentiques au feu de bois avec des ingrédients frais', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1066.jpg', '/uploads/restaurants/restaurant_1066_icon.jpg', FALSE, TRUE, TRUE, 4.99, '18-47 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1066, 'PIZZA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1066, 'Pizza Dishes', 'Pizza Dishes', 'Plats pizza', 'أطباق PIZZA', 1066);

-- Menu item: Margherita Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1203, 'Margherita Pizza', 'Margherita Pizza', 'Pizza Margherita', 'بيتزا مارغريتا', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', TRUE, 12.5, TRUE, 1066);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1203, '/uploads/menu-items/item_1203.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1203, 1066);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1406, 1203, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2218, 1406, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2219, 1406, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2220, 1406, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1407, 1203, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2221, 1407, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2222, 1407, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2223, 1407, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Pepperoni Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1204, 'Pepperoni Pizza', 'Pepperoni Pizza', 'Pizza Pepperoni', 'بيتزا ببروني', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', FALSE, 14.0, TRUE, 1066);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1204, '/uploads/menu-items/item_1204.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1204, 1066);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1408, 1204, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2224, 1408, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2225, 1408, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2226, 1408, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1409, 1204, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2227, 1409, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2228, 1409, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2229, 1409, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Vegetarian Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1205, 'Vegetarian Pizza', 'Vegetarian Pizza', 'Pizza Végétarienne', 'بيتزا نباتية', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', FALSE, 13.0, TRUE, 1066);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1205, '/uploads/menu-items/item_1205.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1205, 1066);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1410, 1205, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2230, 1410, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2231, 1410, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2232, 1410, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1411, 1205, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2233, 1411, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2234, 1411, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2235, 1411, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 68: French Bakery Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1067, 'French Bakery Express', 'French Bakery Express', 'French Bakery Express', 'مطعم French Bakery', 'Rue des Roses, Menzah 8', '+216 28 351 934', 3.8, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1067.jpg', '/uploads/restaurants/restaurant_1067_icon.jpg', FALSE, FALSE, FALSE, 3.21, '19-37 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1067, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1067, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1067);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1206, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1067);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1206, '/uploads/menu-items/item_1206.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1206, 1067);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1412, 1206, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2236, 1412, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2237, 1412, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2238, 1412, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1413, 1206, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2239, 1413, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2240, 1413, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2241, 1413, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1207, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1067);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1207, '/uploads/menu-items/item_1207.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1207, 1067);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1414, 1207, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2242, 1414, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2243, 1414, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2244, 1414, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1415, 1207, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2245, 1415, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2246, 1415, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2247, 1415, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1208, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1067);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1208, '/uploads/menu-items/item_1208.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1208, 1067);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1416, 1208, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2248, 1416, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2249, 1416, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2250, 1416, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1417, 1208, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2251, 1417, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2252, 1417, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2253, 1417, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 69: French Bakery Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1068, 'French Bakery Express', 'French Bakery Express', 'French Bakery Express', 'مطعم French Bakery', 'Avenue de Carthage, Aouina', '+216 28 777 405', 4.3, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1068.jpg', '/uploads/restaurants/restaurant_1068_icon.jpg', FALSE, FALSE, FALSE, 1.4, '17-53 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1068, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1068, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1068);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1209, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1068);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1209, '/uploads/menu-items/item_1209.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1209, 1068);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1418, 1209, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2254, 1418, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2255, 1418, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2256, 1418, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1419, 1209, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2257, 1419, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2258, 1419, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2259, 1419, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1210, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1068);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1210, '/uploads/menu-items/item_1210.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1210, 1068);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1420, 1210, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2260, 1420, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2261, 1420, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2262, 1420, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1421, 1210, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2263, 1421, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2264, 1421, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2265, 1421, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1211, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1068);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1211, '/uploads/menu-items/item_1211.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1211, 1068);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1422, 1211, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2266, 1422, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2267, 1422, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2268, 1422, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1423, 1211, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2269, 1423, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2270, 1423, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2271, 1423, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 70: Roma Restaurant Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1069, 'Roma Restaurant Centre', 'Roma Restaurant Centre', 'Roma Restaurant Centre', 'مطعم Roma Restaurant', 'Boulevard de l'Aéroport, Aouina', '+216 29 577 955', 4.7, 'Traditional Italian dishes prepared with love', 'Traditional Italian dishes prepared with love', 'Plats italiens traditionnels préparés avec amour', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1069.jpg', '/uploads/restaurants/restaurant_1069_icon.jpg', FALSE, FALSE, FALSE, 3.48, '26-48 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1069, 'ITALIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1069, 'Italian Dishes', 'Italian Dishes', 'Plats italian', 'أطباق ITALIAN', 1069);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1212, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1069);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1212, '/uploads/menu-items/item_1212.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1212, 1069);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1424, 1212, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2272, 1424, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2273, 1424, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2274, 1424, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1425, 1212, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2275, 1425, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2276, 1425, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2277, 1425, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1213, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1069);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1213, '/uploads/menu-items/item_1213.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1213, 1069);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1426, 1213, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2278, 1426, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2279, 1426, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2280, 1426, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1427, 1213, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2281, 1427, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2282, 1427, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2283, 1427, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1214, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1069);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1214, '/uploads/menu-items/item_1214.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1214, 1069);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1428, 1214, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2284, 1428, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2285, 1428, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2286, 1428, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1429, 1214, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2287, 1429, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2288, 1429, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2289, 1429, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 71: Eatery Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1070, 'Eatery Ville', 'Eatery Ville', 'Eatery Ville', 'مطعم Eatery', 'Boulevard de l'Environnement, Menzah 9', '+216 22 388 401', 5.0, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8167, 10.2, '/uploads/restaurants/restaurant_1070.jpg', '/uploads/restaurants/restaurant_1070_icon.jpg', FALSE, FALSE, FALSE, 2.05, '26-45 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1070, 'MEXICAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1070, 'Mexican Dishes', 'Mexican Dishes', 'Plats mexican', 'أطباق MEXICAN', 1070);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1215, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1070);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1215, '/uploads/menu-items/item_1215.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1215, 1070);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1430, 1215, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2290, 1430, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2291, 1430, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2292, 1430, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1431, 1215, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2293, 1431, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2294, 1431, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2295, 1431, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1216, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1070);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1216, '/uploads/menu-items/item_1216.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1216, 1070);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1432, 1216, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2296, 1432, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2297, 1432, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2298, 1432, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1433, 1216, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2299, 1433, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2300, 1433, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2301, 1433, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1217, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1070);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1217, '/uploads/menu-items/item_1217.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1217, 1070);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1434, 1217, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2302, 1434, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2303, 1434, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2304, 1434, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1435, 1217, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2305, 1435, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2306, 1435, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2307, 1435, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 72: Sushi Bar Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1071, 'Sushi Bar Plus', 'Sushi Bar Plus', 'Sushi Bar Plus', 'مطعم Sushi Bar', 'Rue des Jasmins, Ariana Riadh', '+216 23 111 490', 4.4, 'Fresh sushi and Japanese specialties', 'Fresh sushi and Japanese specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8419, 10.1958, '/uploads/restaurants/restaurant_1071.jpg', '/uploads/restaurants/restaurant_1071_icon.jpg', TRUE, FALSE, FALSE, 2.07, '21-41 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1071, 'SUSHI');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1071, 'Sushi Dishes', 'Sushi Dishes', 'Plats sushi', 'أطباق SUSHI', 1071);

-- Menu item: California Roll
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1218, 'California Roll', 'California Roll', 'California Roll', 'كاليفورنيا رول', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', 'Crab, avocado, cucumber', TRUE, 12.0, TRUE, 1071);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1218, '/uploads/menu-items/item_1218.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1218, 1071);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1436, 1218, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2308, 1436, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2309, 1436, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2310, 1436, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1437, 1218, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2311, 1437, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2312, 1437, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2313, 1437, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Salmon Sushi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1219, 'Salmon Sushi', 'Salmon Sushi', 'Sushi Saumon', 'سوشي السلمون', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', 'Fresh salmon nigiri', FALSE, 14.0, TRUE, 1071);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1219, '/uploads/menu-items/item_1219.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1219, 1071);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1438, 1219, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2314, 1438, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2315, 1438, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2316, 1438, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1439, 1219, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2317, 1439, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2318, 1439, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2319, 1439, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Tuna Sashimi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1220, 'Tuna Sashimi', 'Tuna Sashimi', 'Sashimi Thon', 'ساشيمي التونة', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', 'Fresh tuna slices', FALSE, 16.0, TRUE, 1071);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1220, '/uploads/menu-items/item_1220.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1220, 1071);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1440, 1220, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2320, 1440, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2321, 1440, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2322, 1440, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1441, 1220, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2323, 1441, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2324, 1441, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2325, 1441, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Mixed Sushi Platter
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1221, 'Mixed Sushi Platter', 'Mixed Sushi Platter', 'Plateau Mixte Sushi', 'طبق سوشي مشكل', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', 'Assorted sushi and rolls', FALSE, 22.0, TRUE, 1071);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1221, '/uploads/menu-items/item_1221.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1221, 1071);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1442, 1221, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2326, 1442, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2327, 1442, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2328, 1442, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1443, 1221, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2329, 1443, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2330, 1443, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2331, 1443, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 73: Restaurant Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1072, 'Restaurant Premium', 'Restaurant Premium', 'Restaurant Premium', 'مطعم Restaurant', 'Avenue Habib Bourguiba, Ariana', '+216 24 869 743', 4.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1072.jpg', '/uploads/restaurants/restaurant_1072_icon.jpg', FALSE, FALSE, FALSE, 1.59, '30-46 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1072, 'TURKISH');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1072, 'Turkish Dishes', 'Turkish Dishes', 'Plats turkish', 'أطباق TURKISH', 1072);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1222, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1072);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1222, '/uploads/menu-items/item_1222.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1222, 1072);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1444, 1222, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2332, 1444, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2333, 1444, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2334, 1444, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1445, 1222, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2335, 1445, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2336, 1445, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2337, 1445, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1223, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1072);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1223, '/uploads/menu-items/item_1223.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1223, 1072);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1446, 1223, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2338, 1446, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2339, 1446, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2340, 1446, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1447, 1223, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2341, 1447, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2342, 1447, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2343, 1447, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1224, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1072);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1224, '/uploads/menu-items/item_1224.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1224, 1072);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1448, 1224, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2344, 1448, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2345, 1448, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2346, 1448, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1449, 1224, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2347, 1449, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2348, 1449, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2349, 1449, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 74: Bistro Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1073, 'Bistro Centre', 'Bistro Centre', 'Bistro Centre', 'مطعم Bistro', 'Rue de Tunis, Aouina', '+216 20 169 543', 4.7, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1073.jpg', '/uploads/restaurants/restaurant_1073_icon.jpg', FALSE, FALSE, FALSE, 1.51, '30-53 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1073, 'INTERNATIONAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1073, 'International Dishes', 'International Dishes', 'Plats international', 'أطباق INTERNATIONAL', 1073);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1225, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1073);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1225, '/uploads/menu-items/item_1225.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1225, 1073);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1450, 1225, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2350, 1450, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2351, 1450, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2352, 1450, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1451, 1225, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2353, 1451, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2354, 1451, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2355, 1451, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1226, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1073);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1226, '/uploads/menu-items/item_1226.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1226, 1073);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1452, 1226, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2356, 1452, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2357, 1452, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2358, 1452, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1453, 1226, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2359, 1453, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2360, 1453, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2361, 1453, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1227, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1073);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1227, '/uploads/menu-items/item_1227.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1227, 1073);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1454, 1227, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2362, 1454, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2363, 1454, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2364, 1454, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1455, 1227, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2365, 1455, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2366, 1455, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2367, 1455, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 75: La Piazza Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1074, 'La Piazza Palace', 'La Piazza Palace', 'La Piazza Palais', 'مطعم La Piazza', 'Rue des Orangers, Menzah 6', '+216 22 654 955', 4.2, 'Traditional Italian dishes prepared with love', 'Traditional Italian dishes prepared with love', 'Plats italiens traditionnels préparés avec amour', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1074.jpg', '/uploads/restaurants/restaurant_1074_icon.jpg', TRUE, FALSE, FALSE, 3.88, '30-39 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1074, 'ITALIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1074, 'Italian Dishes', 'Italian Dishes', 'Plats italian', 'أطباق ITALIAN', 1074);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1228, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1074);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1228, '/uploads/menu-items/item_1228.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1228, 1074);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1456, 1228, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2368, 1456, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2369, 1456, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2370, 1456, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1457, 1228, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2371, 1457, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2372, 1457, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2373, 1457, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1229, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1074);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1229, '/uploads/menu-items/item_1229.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1229, 1074);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1458, 1229, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2374, 1458, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2375, 1458, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2376, 1458, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1459, 1229, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2377, 1459, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2378, 1459, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2379, 1459, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1230, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1074);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1230, '/uploads/menu-items/item_1230.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1230, 1074);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1460, 1230, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2380, 1460, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2381, 1460, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2382, 1460, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1461, 1230, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2383, 1461, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2384, 1461, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2385, 1461, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 76: Pizza Express Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1075, 'Pizza Express Royal', 'Pizza Express Royal', 'Pizza Express Royal', 'مطعم Pizza Express', 'Avenue Habib Bourguiba, Ariana', '+216 23 637 391', 4.7, 'Authentic wood-fired pizzas with fresh ingredients', 'Authentic wood-fired pizzas with fresh ingredients', 'Pizzas authentiques au feu de bois avec des ingrédients frais', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1075.jpg', '/uploads/restaurants/restaurant_1075_icon.jpg', FALSE, FALSE, FALSE, 4.02, '20-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1075, 'PIZZA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1075, 'Pizza Dishes', 'Pizza Dishes', 'Plats pizza', 'أطباق PIZZA', 1075);

-- Menu item: Vegetarian Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1231, 'Vegetarian Pizza', 'Vegetarian Pizza', 'Pizza Végétarienne', 'بيتزا نباتية', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', 'Fresh vegetables with cheese', TRUE, 13.0, TRUE, 1075);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1231, '/uploads/menu-items/item_1231.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1231, 1075);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1462, 1231, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2386, 1462, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2387, 1462, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2388, 1462, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1463, 1231, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2389, 1463, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2390, 1463, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2391, 1463, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Margherita Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1232, 'Margherita Pizza', 'Margherita Pizza', 'Pizza Margherita', 'بيتزا مارغريتا', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', 'Classic tomato and mozzarella', FALSE, 12.5, TRUE, 1075);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1232, '/uploads/menu-items/item_1232.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1232, 1075);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1464, 1232, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2392, 1464, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2393, 1464, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2394, 1464, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1465, 1232, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2395, 1465, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2396, 1465, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2397, 1465, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Pepperoni Pizza
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1233, 'Pepperoni Pizza', 'Pepperoni Pizza', 'Pizza Pepperoni', 'بيتزا ببروني', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', 'Spicy pepperoni with cheese', FALSE, 14.0, TRUE, 1075);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1233, '/uploads/menu-items/item_1233.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1233, 1075);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1466, 1233, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2398, 1466, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2399, 1466, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2400, 1466, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1467, 1233, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2401, 1467, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2402, 1467, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2403, 1467, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 77: Café Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1076, 'Café Centre', 'Café Centre', 'Café Centre', 'مطعم Café', 'Rue de la République, Ariana', '+216 22 736 225', 3.8, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1076.jpg', '/uploads/restaurants/restaurant_1076_icon.jpg', FALSE, FALSE, FALSE, 3.97, '17-36 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1076, 'SADWICH');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1076, 'Sadwich Dishes', 'Sadwich Dishes', 'Plats sadwich', 'أطباق SADWICH', 1076);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1234, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1076);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1234, '/uploads/menu-items/item_1234.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1234, 1076);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1468, 1234, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2404, 1468, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2405, 1468, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2406, 1468, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1469, 1234, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2407, 1469, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2408, 1469, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2409, 1469, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1235, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1076);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1235, '/uploads/menu-items/item_1235.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1235, 1076);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1470, 1235, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2410, 1470, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2411, 1470, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2412, 1470, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1471, 1235, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2413, 1471, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2414, 1471, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2415, 1471, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1236, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1076);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1236, '/uploads/menu-items/item_1236.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1236, 1076);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1472, 1236, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2416, 1472, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2417, 1472, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2418, 1472, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1473, 1236, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2419, 1473, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2420, 1473, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2421, 1473, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 78: Charcoal Grill Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1077, 'Charcoal Grill Palace', 'Charcoal Grill Palace', 'Charcoal Grill Palais', 'مطعم Charcoal Grill', 'Avenue de la Liberté, Menzah 7', '+216 25 650 431', 4.1, 'Grilled meats and BBQ specialties', 'Grilled meats and BBQ specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1077.jpg', '/uploads/restaurants/restaurant_1077_icon.jpg', FALSE, FALSE, FALSE, 4.39, '25-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1077, 'GRILL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1077, 'Grill Dishes', 'Grill Dishes', 'Plats grill', 'أطباق GRILL', 1077);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1237, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1077);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1237, '/uploads/menu-items/item_1237.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1237, 1077);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1474, 1237, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2422, 1474, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2423, 1474, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2424, 1474, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1475, 1237, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2425, 1475, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2426, 1475, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2427, 1475, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1238, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1077);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1238, '/uploads/menu-items/item_1238.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1238, 1077);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1476, 1238, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2428, 1476, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2429, 1476, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2430, 1476, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1477, 1238, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2431, 1477, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2432, 1477, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2433, 1477, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1239, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1077);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1239, '/uploads/menu-items/item_1239.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1239, 1077);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1478, 1239, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2434, 1478, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2435, 1478, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2436, 1478, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1479, 1239, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2437, 1479, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2438, 1479, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2439, 1479, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 79: Restaurant Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1078, 'Restaurant Plus', 'Restaurant Plus', 'Restaurant Plus', 'مطعم Restaurant', 'Rue des Orangers, Menzah 6', '+216 26 741 395', 4.3, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1078.jpg', '/uploads/restaurants/restaurant_1078_icon.jpg', FALSE, TRUE, FALSE, 4.98, '16-38 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1078, 'INDIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1078, 'Indian Dishes', 'Indian Dishes', 'Plats indian', 'أطباق INDIAN', 1078);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1240, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1078);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1240, '/uploads/menu-items/item_1240.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1240, 1078);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1480, 1240, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2440, 1480, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2441, 1480, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2442, 1480, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1481, 1240, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2443, 1481, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2444, 1481, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2445, 1481, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1241, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1078);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1241, '/uploads/menu-items/item_1241.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1241, 1078);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1482, 1241, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2446, 1482, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2447, 1482, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2448, 1482, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1483, 1241, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2449, 1483, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2450, 1483, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2451, 1483, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1242, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1078);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1242, '/uploads/menu-items/item_1242.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1242, 1078);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1484, 1242, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2452, 1484, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2453, 1484, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2454, 1484, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1485, 1242, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2455, 1485, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2456, 1485, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2457, 1485, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 80: Eatery Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1079, 'Eatery Palace', 'Eatery Palace', 'Eatery Palais', 'مطعم Eatery', 'Rue de la République, Ariana', '+216 22 489 325', 4.0, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1079.jpg', '/uploads/restaurants/restaurant_1079_icon.jpg', FALSE, FALSE, FALSE, 3.24, '21-42 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1079, 'ORIENTAL');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1079, 'Oriental Dishes', 'Oriental Dishes', 'Plats oriental', 'أطباق ORIENTAL', 1079);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1243, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1079);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1243, '/uploads/menu-items/item_1243.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1243, 1079);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1486, 1243, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2458, 1486, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2459, 1486, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2460, 1486, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1487, 1243, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2461, 1487, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2462, 1487, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2463, 1487, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1244, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1079);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1244, '/uploads/menu-items/item_1244.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1244, 1079);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1488, 1244, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2464, 1488, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2465, 1488, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2466, 1488, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1489, 1244, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2467, 1489, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2468, 1489, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2469, 1489, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1245, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1079);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1245, '/uploads/menu-items/item_1245.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1245, 1079);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1490, 1245, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2470, 1490, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2471, 1490, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2472, 1490, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1491, 1245, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2473, 1491, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2474, 1491, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2475, 1491, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 81: Restaurant Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1080, 'Restaurant Ville', 'Restaurant Ville', 'Restaurant Ville', 'مطعم Restaurant', 'Boulevard de l'Aéroport, Aouina', '+216 20 923 859', 4.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1080.jpg', '/uploads/restaurants/restaurant_1080_icon.jpg', FALSE, FALSE, FALSE, 3.55, '19-53 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1080, 'MEXICAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1080, 'Mexican Dishes', 'Mexican Dishes', 'Plats mexican', 'أطباق MEXICAN', 1080);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1246, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1080);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1246, '/uploads/menu-items/item_1246.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1246, 1080);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1492, 1246, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2476, 1492, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2477, 1492, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2478, 1492, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1493, 1246, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2479, 1493, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2480, 1493, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2481, 1493, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1247, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1080);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1247, '/uploads/menu-items/item_1247.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1247, 1080);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1494, 1247, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2482, 1494, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2483, 1494, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2484, 1494, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1495, 1247, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2485, 1495, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2486, 1495, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2487, 1495, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1248, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1080);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1248, '/uploads/menu-items/item_1248.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1248, 1080);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1496, 1248, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2488, 1496, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2489, 1496, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2490, 1496, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1497, 1248, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2491, 1497, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2492, 1497, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2493, 1497, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 82: Bella Italia Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1081, 'Bella Italia Premium', 'Bella Italia Premium', 'Bella Italia Premium', 'مطعم Bella Italia', 'Rue des Jasmins, Ariana Riadh', '+216 27 619 439', 4.6, 'Traditional Italian dishes prepared with love', 'Traditional Italian dishes prepared with love', 'Plats italiens traditionnels préparés avec amour', 'مطعم يقدم أطباق عالية الجودة', 36.8419, 10.1958, '/uploads/restaurants/restaurant_1081.jpg', '/uploads/restaurants/restaurant_1081_icon.jpg', FALSE, TRUE, FALSE, 2.24, '25-38 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1081, 'ITALIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1081, 'Italian Dishes', 'Italian Dishes', 'Plats italian', 'أطباق ITALIAN', 1081);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1249, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1081);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1249, '/uploads/menu-items/item_1249.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1249, 1081);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1498, 1249, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2494, 1498, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2495, 1498, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2496, 1498, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1499, 1249, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2497, 1499, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2498, 1499, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2499, 1499, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1250, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1081);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1250, '/uploads/menu-items/item_1250.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1250, 1081);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1500, 1250, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2500, 1500, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2501, 1500, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2502, 1500, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1501, 1250, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2503, 1501, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2504, 1501, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2505, 1501, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1251, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1081);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1251, '/uploads/menu-items/item_1251.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1251, 1081);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1502, 1251, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2506, 1502, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2507, 1502, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2508, 1502, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1503, 1251, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2509, 1503, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2510, 1503, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2511, 1503, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 83: Chicken Express Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1082, 'Chicken Express Ville', 'Chicken Express Ville', 'Chicken Express Ville', 'مطعم Chicken Express', 'Avenue Mohamed V, Menzah 5', '+216 24 548 174', 4.3, 'Grilled and fried chicken specialties', 'Grilled and fried chicken specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1082.jpg', '/uploads/restaurants/restaurant_1082_icon.jpg', FALSE, FALSE, FALSE, 4.18, '25-60 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1082, 'CHICKEN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1082, 'Chicken Dishes', 'Chicken Dishes', 'Plats chicken', 'أطباق CHICKEN', 1082);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1252, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1082);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1252, '/uploads/menu-items/item_1252.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1252, 1082);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1504, 1252, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2512, 1504, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2513, 1504, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2514, 1504, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1505, 1252, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2515, 1505, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2516, 1505, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2517, 1505, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1253, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1082);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1253, '/uploads/menu-items/item_1253.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1253, 1082);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1506, 1253, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2518, 1506, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2519, 1506, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2520, 1506, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1507, 1253, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2521, 1507, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2522, 1507, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2523, 1507, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1254, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1082);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1254, '/uploads/menu-items/item_1254.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1254, 1082);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1508, 1254, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2524, 1508, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2525, 1508, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2526, 1508, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1509, 1254, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2527, 1509, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2528, 1509, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2529, 1509, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 84: Tunisian Kitchen Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1083, 'Tunisian Kitchen Centre', 'Tunisian Kitchen Centre', 'Tunisian Cuisine Centre', 'مطعم Tunisian Kitchen', 'Boulevard de l'Aéroport, Aouina', '+216 20 843 560', 3.9, 'Authentic Tunisian cuisine with traditional recipes', 'Authentic Tunisian cuisine with traditional recipes', 'Cuisine tunisienne authentique avec des recettes traditionnelles', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1083.jpg', '/uploads/restaurants/restaurant_1083_icon.jpg', FALSE, FALSE, FALSE, 1.37, '25-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1083, 'TUNISIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1083, 'Tunisian Dishes', 'Tunisian Dishes', 'Plats tunisian', 'أطباق TUNISIAN', 1083);

-- Menu item: Ojja
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1255, 'Ojja', 'Ojja', 'Ojja', 'عجة', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', TRUE, 7.0, TRUE, 1083);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1255, '/uploads/menu-items/item_1255.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1255, 1083);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1510, 1255, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2530, 1510, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2531, 1510, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2532, 1510, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1511, 1255, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2533, 1511, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2534, 1511, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2535, 1511, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Brik
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1256, 'Brik', 'Brik', 'Brik', 'بريك', 'Crispy pastry with egg and tuna', 'Crispy pastry with egg and tuna', 'Crispy pastry with egg and tuna', 'Crispy pastry with egg and tuna', FALSE, 5.5, TRUE, 1083);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1256, '/uploads/menu-items/item_1256.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1256, 1083);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1512, 1256, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2536, 1512, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2537, 1512, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2538, 1512, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1513, 1256, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2539, 1513, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2540, 1513, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2541, 1513, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Lablabi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1257, 'Lablabi', 'Lablabi', 'Lablabi', 'لبلابي', 'Chickpea soup with bread', 'Chickpea soup with bread', 'Chickpea soup with bread', 'Chickpea soup with bread', FALSE, 4.5, TRUE, 1083);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1257, '/uploads/menu-items/item_1257.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1257, 1083);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1514, 1257, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2542, 1514, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2543, 1514, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2544, 1514, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1515, 1257, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2545, 1515, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2546, 1515, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2547, 1515, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 85: Tacos Corner Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1084, 'Tacos Corner Centre', 'Tacos Corner Centre', 'Tacos Coin Centre', 'مطعم Tacos Corner', 'Boulevard de l'Aéroport, Aouina', '+216 26 199 115', 4.7, 'Mexican tacos with fresh ingredients', 'Mexican tacos with fresh ingredients', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.84, 10.22, '/uploads/restaurants/restaurant_1084.jpg', '/uploads/restaurants/restaurant_1084_icon.jpg', FALSE, FALSE, FALSE, 3.72, '20-40 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1084, 'TACOS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1084, 'Tacos Dishes', 'Tacos Dishes', 'Plats tacos', 'أطباق TACOS', 1084);

-- Menu item: Beef Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1258, 'Beef Tacos', 'Beef Tacos', 'Tacos Boeuf', 'تاكو باللحم', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', 'Seasoned beef with toppings', TRUE, 8.0, TRUE, 1084);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1258, '/uploads/menu-items/item_1258.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1258, 1084);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1516, 1258, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2548, 1516, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2549, 1516, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2550, 1516, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1517, 1258, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2551, 1517, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2552, 1517, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2553, 1517, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Fish Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1259, 'Fish Tacos', 'Fish Tacos', 'Tacos Poisson', 'تاكو بالسمك', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', 'Crispy fish with sauce', FALSE, 9.0, TRUE, 1084);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1259, '/uploads/menu-items/item_1259.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1259, 1084);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1518, 1259, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2554, 1518, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2555, 1518, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2556, 1518, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1519, 1259, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2557, 1519, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2558, 1519, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2559, 1519, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Chicken Tacos
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1260, 'Chicken Tacos', 'Chicken Tacos', 'Tacos Poulet', 'تاكو بالدجاج', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', 'Grilled chicken with salsa', FALSE, 7.5, TRUE, 1084);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1260, '/uploads/menu-items/item_1260.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1260, 1084);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1520, 1260, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2560, 1520, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2561, 1520, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2562, 1520, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1521, 1260, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2563, 1521, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2564, 1521, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2565, 1521, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 86: Marine Restaurant Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1085, 'Marine Restaurant Premium', 'Marine Restaurant Premium', 'Marine Restaurant Premium', 'مطعم Marine Restaurant', 'Rue de la République, Ariana', '+216 23 969 537', 3.6, 'Fresh seafood from the Mediterranean', 'Fresh seafood from the Mediterranean', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8667, 10.1833, '/uploads/restaurants/restaurant_1085.jpg', '/uploads/restaurants/restaurant_1085_icon.jpg', FALSE, FALSE, FALSE, 1.01, '25-37 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1085, 'SEAFOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1085, 'Seafood Dishes', 'Seafood Dishes', 'Plats seafood', 'أطباق SEAFOOD', 1085);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1261, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1085);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1261, '/uploads/menu-items/item_1261.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1261, 1085);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1522, 1261, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2566, 1522, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2567, 1522, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2568, 1522, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1523, 1261, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2569, 1523, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2570, 1523, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2571, 1523, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1262, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1085);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1262, '/uploads/menu-items/item_1262.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1262, 1085);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1524, 1262, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2572, 1524, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2573, 1524, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2574, 1524, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1525, 1262, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2575, 1525, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2576, 1525, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2577, 1525, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1263, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1085);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1263, '/uploads/menu-items/item_1263.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1263, 1085);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1526, 1263, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2578, 1526, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2579, 1526, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2580, 1526, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1527, 1263, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2581, 1527, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2582, 1527, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2583, 1527, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 87: Café Central Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1086, 'Café Central Centre', 'Café Central Centre', 'Café Central Centre', 'مطعم Café Central', 'Avenue de la Liberté, Ariana Essoughra', '+216 24 647 305', 4.2, 'Quality coffee and tea in a cozy atmosphere', 'Quality coffee and tea in a cozy atmosphere', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1086.jpg', '/uploads/restaurants/restaurant_1086_icon.jpg', FALSE, TRUE, TRUE, 4.29, '24-44 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1086, 'TEA_COFFEE');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1086, 'Tea Coffee Dishes', 'Tea Coffee Dishes', 'Plats tea coffee', 'أطباق TEA_COFFEE', 1086);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1264, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1086);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1264, '/uploads/menu-items/item_1264.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1264, 1086);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1528, 1264, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2584, 1528, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2585, 1528, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2586, 1528, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1529, 1264, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2587, 1529, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2588, 1529, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2589, 1529, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1265, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1086);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1265, '/uploads/menu-items/item_1265.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1265, 1086);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1530, 1265, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2590, 1530, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2591, 1530, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2592, 1530, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1531, 1265, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2593, 1531, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2594, 1531, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2595, 1531, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1266, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1086);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1266, '/uploads/menu-items/item_1266.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1266, 1086);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1532, 1266, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2596, 1532, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2597, 1532, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2598, 1532, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1533, 1266, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2599, 1533, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2600, 1533, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2601, 1533, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 88: Express Meal Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1087, 'Express Meal Centre', 'Express Meal Centre', 'Express Meal Centre', 'مطعم Express Meal', 'Rue des Roses, Menzah 8', '+216 28 871 313', 4.5, 'Quick and tasty meals for people on the go', 'Quick and tasty meals for people on the go', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1087.jpg', '/uploads/restaurants/restaurant_1087_icon.jpg', TRUE, FALSE, FALSE, 4.1, '18-59 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1087, 'FAST_FOOD');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1087, 'Fast Food Dishes', 'Fast Food Dishes', 'Plats fast food', 'أطباق FAST_FOOD', 1087);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1267, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1087);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1267, '/uploads/menu-items/item_1267.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1267, 1087);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1534, 1267, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2602, 1534, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2603, 1534, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2604, 1534, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1535, 1267, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2605, 1535, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2606, 1535, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2607, 1535, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1268, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1087);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1268, '/uploads/menu-items/item_1268.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1268, 1087);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1536, 1268, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2608, 1536, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2609, 1536, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2610, 1536, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1537, 1268, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2611, 1537, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2612, 1537, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2613, 1537, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1269, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1087);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1269, '/uploads/menu-items/item_1269.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1269, 1087);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1538, 1269, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2614, 1538, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2615, 1538, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2616, 1538, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1539, 1269, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2617, 1539, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2618, 1539, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2619, 1539, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 89: Restaurant Centre
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1088, 'Restaurant Centre', 'Restaurant Centre', 'Restaurant Centre', 'مطعم Restaurant', 'Avenue de la Liberté, Ariana Essoughra', '+216 27 195 878', 3.7, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1088.jpg', '/uploads/restaurants/restaurant_1088_icon.jpg', FALSE, FALSE, FALSE, 1.25, '28-43 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1088, 'SALDAS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1088, 'Saldas Dishes', 'Saldas Dishes', 'Plats saldas', 'أطباق SALDAS', 1088);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1270, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1088);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1270, '/uploads/menu-items/item_1270.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1270, 1088);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1540, 1270, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2620, 1540, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2621, 1540, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2622, 1540, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1541, 1270, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2623, 1541, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2624, 1541, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2625, 1541, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1271, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1088);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1271, '/uploads/menu-items/item_1271.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1271, 1088);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1542, 1271, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2626, 1542, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2627, 1542, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2628, 1542, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1543, 1271, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2629, 1543, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2630, 1543, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2631, 1543, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1272, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1088);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1272, '/uploads/menu-items/item_1272.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1272, 1088);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1544, 1272, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2632, 1544, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2633, 1544, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2634, 1544, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1545, 1272, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2635, 1545, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2636, 1545, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2637, 1545, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 90: Eatery Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1089, 'Eatery Premium', 'Eatery Premium', 'Eatery Premium', 'مطعم Eatery', 'Avenue de la Liberté, Menzah 7', '+216 25 436 949', 4.2, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1089.jpg', '/uploads/restaurants/restaurant_1089_icon.jpg', FALSE, FALSE, FALSE, 2.56, '23-54 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1089, 'SALDAS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1089, 'Saldas Dishes', 'Saldas Dishes', 'Plats saldas', 'أطباق SALDAS', 1089);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1273, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1089);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1273, '/uploads/menu-items/item_1273.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1273, 1089);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1546, 1273, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2638, 1546, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2639, 1546, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2640, 1546, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1547, 1273, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2641, 1547, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2642, 1547, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2643, 1547, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1274, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1089);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1274, '/uploads/menu-items/item_1274.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1274, 1089);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1548, 1274, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2644, 1548, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2645, 1548, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2646, 1548, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1549, 1274, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2647, 1549, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2648, 1549, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2649, 1549, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1275, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1089);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1275, '/uploads/menu-items/item_1275.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1275, 1089);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1550, 1275, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2650, 1550, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2651, 1550, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2652, 1550, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1551, 1275, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2653, 1551, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2654, 1551, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2655, 1551, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 91: Wok House Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1090, 'Wok House Ville', 'Wok House Ville', 'Wok Maison Ville', 'مطعم Wok House', 'Avenue de la Liberté, Ariana Essoughra', '+216 29 901 103', 4.1, 'Delicious Asian cuisine with authentic flavors', 'Delicious Asian cuisine with authentic flavors', 'Délicieuse cuisine asiatique aux saveurs authentiques', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1090.jpg', '/uploads/restaurants/restaurant_1090_icon.jpg', TRUE, TRUE, FALSE, 3.91, '29-43 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1090, 'ASIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1090, 'Asian Dishes', 'Asian Dishes', 'Plats asian', 'أطباق ASIAN', 1090);

-- Menu item: Fried Rice
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1276, 'Fried Rice', 'Fried Rice', 'Riz Frit', 'أرز مقلي', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', 'Wok-fried rice with vegetables', TRUE, 8.5, TRUE, 1090);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1276, '/uploads/menu-items/item_1276.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1276, 1090);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1552, 1276, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2656, 1552, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2657, 1552, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2658, 1552, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1553, 1276, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2659, 1553, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2660, 1553, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2661, 1553, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Noodles
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1277, 'Noodles', 'Noodles', 'Nouilles', 'نودلز', 'Stir-fried noodles with sauce', 'Stir-fried noodles with sauce', 'Stir-fried noodles with sauce', 'Stir-fried noodles with sauce', FALSE, 9.0, TRUE, 1090);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1277, '/uploads/menu-items/item_1277.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1277, 1090);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1554, 1277, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2662, 1554, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2663, 1554, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2664, 1554, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1555, 1277, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2665, 1555, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2666, 1555, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2667, 1555, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Sweet & Sour Chicken
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1278, 'Sweet & Sour Chicken', 'Sweet & Sour Chicken', 'Poulet Aigre-Doux', 'دجاج حلو وحامض', 'Chicken in sweet and sour sauce', 'Chicken in sweet and sour sauce', 'Chicken in sweet and sour sauce', 'Chicken in sweet and sour sauce', FALSE, 11.0, TRUE, 1090);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1278, '/uploads/menu-items/item_1278.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1278, 1090);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1556, 1278, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2668, 1556, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2669, 1556, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2670, 1556, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1557, 1278, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2671, 1557, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2672, 1557, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2673, 1557, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 92: Le Traditionnel Express
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1091, 'Le Traditionnel Express', 'Le Traditionnel Express', 'Le Traditionnel Express', 'مطعم Le Traditionnel', 'Rue des Roses, Menzah 8', '+216 21 835 462', 3.5, 'Authentic Tunisian cuisine with traditional recipes', 'Authentic Tunisian cuisine with traditional recipes', 'Cuisine tunisienne authentique avec des recettes traditionnelles', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1091.jpg', '/uploads/restaurants/restaurant_1091_icon.jpg', FALSE, FALSE, FALSE, 1.85, '29-49 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1091, 'TUNISIAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1091, 'Tunisian Dishes', 'Tunisian Dishes', 'Plats tunisian', 'أطباق TUNISIAN', 1091);

-- Menu item: Couscous
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1279, 'Couscous', 'Couscous', 'Couscous', 'كسكس', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', 'Traditional couscous with vegetables', TRUE, 10.0, TRUE, 1091);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1279, '/uploads/menu-items/item_1279.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1279, 1091);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1558, 1279, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2674, 1558, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2675, 1558, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2676, 1558, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1559, 1279, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2677, 1559, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2678, 1559, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2679, 1559, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Ojja
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1280, 'Ojja', 'Ojja', 'Ojja', 'عجة', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', 'Eggs with tomato and peppers', FALSE, 7.0, TRUE, 1091);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1280, '/uploads/menu-items/item_1280.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1280, 1091);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1560, 1280, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2680, 1560, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2681, 1560, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2682, 1560, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1561, 1280, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2683, 1561, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2684, 1561, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2685, 1561, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Lablabi
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1281, 'Lablabi', 'Lablabi', 'Lablabi', 'لبلابي', 'Chickpea soup with bread', 'Chickpea soup with bread', 'Chickpea soup with bread', 'Chickpea soup with bread', FALSE, 4.5, TRUE, 1091);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1281, '/uploads/menu-items/item_1281.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1281, 1091);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1562, 1281, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2686, 1562, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2687, 1562, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2688, 1562, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1563, 1281, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2689, 1563, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2690, 1563, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2691, 1563, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 93: Café Royal
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1092, 'Café Royal', 'Café Royal', 'Café Royal', 'مطعم Café', 'Avenue de la Liberté, Menzah 7', '+216 26 970 660', 4.5, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8278, 10.1889, '/uploads/restaurants/restaurant_1092.jpg', '/uploads/restaurants/restaurant_1092_icon.jpg', FALSE, FALSE, FALSE, 1.39, '19-41 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1092, 'ICE_CREAM');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1092, 'Ice Cream Dishes', 'Ice Cream Dishes', 'Plats ice cream', 'أطباق ICE_CREAM', 1092);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1282, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1092);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1282, '/uploads/menu-items/item_1282.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1282, 1092);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1564, 1282, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2692, 1564, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2693, 1564, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2694, 1564, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1565, 1282, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2695, 1565, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2696, 1565, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2697, 1565, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1283, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1092);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1283, '/uploads/menu-items/item_1283.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1283, 1092);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1566, 1283, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2698, 1566, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2699, 1566, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2700, 1566, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1567, 1283, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2701, 1567, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2702, 1567, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2703, 1567, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1284, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1092);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1284, '/uploads/menu-items/item_1284.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1284, 1092);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1568, 1284, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2704, 1568, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2705, 1568, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2706, 1568, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1569, 1284, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2707, 1569, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2708, 1569, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2709, 1569, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 94: Italian Pasta Premium
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1093, 'Italian Pasta Premium', 'Italian Pasta Premium', 'Italian Pasta Premium', 'مطعم Italian Pasta', 'Rue des Roses, Menzah 8', '+216 22 152 379', 4.1, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8222, 10.1944, '/uploads/restaurants/restaurant_1093.jpg', '/uploads/restaurants/restaurant_1093_icon.jpg', FALSE, FALSE, TRUE, 1.91, '18-46 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1093, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1093, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1093);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1285, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1093);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1285, '/uploads/menu-items/item_1285.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1285, 1093);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1570, 1285, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2710, 1570, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2711, 1570, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2712, 1570, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1571, 1285, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2713, 1571, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2714, 1571, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2715, 1571, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1286, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1093);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1286, '/uploads/menu-items/item_1286.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1286, 1093);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1572, 1286, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2716, 1572, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2717, 1572, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2718, 1572, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1573, 1286, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2719, 1573, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2720, 1573, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2721, 1573, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1287, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1093);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1287, '/uploads/menu-items/item_1287.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1287, 1093);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1574, 1287, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2722, 1574, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2723, 1574, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2724, 1574, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1575, 1287, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2725, 1575, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2726, 1575, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2727, 1575, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 95: Restaurant Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1094, 'Restaurant Ville', 'Restaurant Ville', 'Restaurant Ville', 'مطعم Restaurant', 'Avenue Habib Bourguiba, Ariana', '+216 21 790 826', 4.5, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8625, 10.1947, '/uploads/restaurants/restaurant_1094.jpg', '/uploads/restaurants/restaurant_1094_icon.jpg', TRUE, TRUE, FALSE, 3.13, '27-50 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1094, 'BREAKFAST');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1094, 'Breakfast Dishes', 'Breakfast Dishes', 'Plats breakfast', 'أطباق BREAKFAST', 1094);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1288, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1094);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1288, '/uploads/menu-items/item_1288.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1288, 1094);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1576, 1288, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2728, 1576, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2729, 1576, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2730, 1576, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1577, 1288, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2731, 1577, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2732, 1577, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2733, 1577, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1289, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1094);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1289, '/uploads/menu-items/item_1289.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1289, 1094);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1578, 1289, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2734, 1578, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2735, 1578, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2736, 1578, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1579, 1289, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2737, 1579, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2738, 1579, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2739, 1579, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1290, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1094);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1290, '/uploads/menu-items/item_1290.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1290, 1094);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1580, 1290, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2740, 1580, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2741, 1580, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2742, 1580, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1581, 1290, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2743, 1581, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2744, 1581, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2745, 1581, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 96: Restaurant Plus
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1095, 'Restaurant Plus', 'Restaurant Plus', 'Restaurant Plus', 'مطعم Restaurant', 'Rue des Orangers, Menzah 6', '+216 24 479 803', 3.6, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8333, 10.1833, '/uploads/restaurants/restaurant_1095.jpg', '/uploads/restaurants/restaurant_1095_icon.jpg', FALSE, FALSE, FALSE, 2.75, '19-55 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1095, 'SNACKS');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1095, 'Snacks Dishes', 'Snacks Dishes', 'Plats snacks', 'أطباق SNACKS', 1095);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1291, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', TRUE, 12.0, TRUE, 1095);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1291, '/uploads/menu-items/item_1291.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1291, 1095);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1582, 1291, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2746, 1582, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2747, 1582, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2748, 1582, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1583, 1291, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2749, 1583, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2750, 1583, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2751, 1583, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1292, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1095);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1292, '/uploads/menu-items/item_1292.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1292, 1095);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1584, 1292, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2752, 1584, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2753, 1584, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2754, 1584, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1585, 1292, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2755, 1585, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2756, 1585, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2757, 1585, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1293, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1095);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1293, '/uploads/menu-items/item_1293.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1293, 1095);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1586, 1293, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2758, 1586, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2759, 1586, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2760, 1586, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1587, 1293, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2761, 1587, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2762, 1587, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2763, 1587, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 97: Italian Pasta Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1096, 'Italian Pasta Palace', 'Italian Pasta Palace', 'Italian Pasta Palais', 'مطعم Italian Pasta', 'Avenue de Carthage, Aouina', '+216 21 911 726', 3.6, 'Fresh homemade pasta dishes', 'Fresh homemade pasta dishes', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8444, 10.2272, '/uploads/restaurants/restaurant_1096.jpg', '/uploads/restaurants/restaurant_1096_icon.jpg', TRUE, TRUE, FALSE, 3.46, '24-50 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1096, 'PASTA');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1096, 'Pasta Dishes', 'Pasta Dishes', 'Plats pasta', 'أطباق PASTA', 1096);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1294, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1096);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1294, '/uploads/menu-items/item_1294.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1294, 1096);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1588, 1294, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2764, 1588, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2765, 1588, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2766, 1588, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1589, 1294, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2767, 1589, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2768, 1589, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2769, 1589, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1295, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1096);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1295, '/uploads/menu-items/item_1295.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1295, 1096);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1590, 1295, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2770, 1590, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2771, 1590, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2772, 1590, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1591, 1295, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2773, 1591, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2774, 1591, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2775, 1591, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1296, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1096);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1296, '/uploads/menu-items/item_1296.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1296, 1096);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1592, 1296, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2776, 1592, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2777, 1592, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2778, 1592, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1593, 1296, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2779, 1593, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2780, 1593, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2781, 1593, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 98: Eatery Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1097, 'Eatery Palace', 'Eatery Palace', 'Eatery Palais', 'مطعم Eatery', 'Rue de Tunis, Aouina', '+216 21 494 215', 4.2, 'Quality food with excellent service', 'Quality food with excellent service', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.85, 10.23, '/uploads/restaurants/restaurant_1097.jpg', '/uploads/restaurants/restaurant_1097_icon.jpg', FALSE, FALSE, FALSE, 3.83, '25-52 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1097, 'MEXICAN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1097, 'Mexican Dishes', 'Mexican Dishes', 'Plats mexican', 'أطباق MEXICAN', 1097);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1297, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1097);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1297, '/uploads/menu-items/item_1297.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1297, 1097);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1594, 1297, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2782, 1594, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2783, 1594, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2784, 1594, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1595, 1297, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2785, 1595, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2786, 1595, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2787, 1595, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1298, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1097);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1298, '/uploads/menu-items/item_1298.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1298, 1097);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1596, 1298, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2788, 1596, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2789, 1596, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2790, 1596, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1597, 1298, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2791, 1597, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2792, 1597, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2793, 1597, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1299, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1097);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1299, '/uploads/menu-items/item_1299.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1299, 1097);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1598, 1299, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2794, 1598, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2795, 1598, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2796, 1598, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1599, 1299, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2797, 1599, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2798, 1599, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2799, 1599, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 99: Chicken Corner Ville
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1098, 'Chicken Corner Ville', 'Chicken Corner Ville', 'Chicken Coin Ville', 'مطعم Chicken Corner', 'Avenue Mohamed V, Menzah 5', '+216 25 405 527', 4.9, 'Grilled and fried chicken specialties', 'Grilled and fried chicken specialties', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8397, 10.1775, '/uploads/restaurants/restaurant_1098.jpg', '/uploads/restaurants/restaurant_1098_icon.jpg', FALSE, FALSE, TRUE, 3.34, '19-38 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1098, 'CHICKEN');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1098, 'Chicken Dishes', 'Chicken Dishes', 'Plats chicken', 'أطباق CHICKEN', 1098);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1300, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', TRUE, 11.0, TRUE, 1098);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1300, '/uploads/menu-items/item_1300.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1300, 1098);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1600, 1300, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2800, 1600, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2801, 1600, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2802, 1600, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1601, 1300, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2803, 1601, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2804, 1601, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2805, 1601, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1301, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1098);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1301, '/uploads/menu-items/item_1301.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1301, 1098);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1602, 1301, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2806, 1602, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2807, 1602, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2808, 1602, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1603, 1301, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2809, 1603, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2810, 1603, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2811, 1603, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1302, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', FALSE, 10.0, TRUE, 1098);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1302, '/uploads/menu-items/item_1302.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1302, 1098);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1604, 1302, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2812, 1604, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2813, 1604, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2814, 1604, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1605, 1302, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2815, 1605, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2816, 1605, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2817, 1605, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Restaurant 100: La Boulangerie Palace
INSERT INTO restaurant (id, name, name_en, name_fr, name_ar, address, phone, rating, description, description_en, description_fr, description_ar, latitude, longitude, image_url, icon_url, top_choice, free_delivery, top_eat, delivery_fee, delivery_time_range, restaurant_share_rate, opening_hours, closing_hours) VALUES
(1099, 'La Boulangerie Palace', 'La Boulangerie Palace', 'La Boulangerie Palais', 'مطعم La Boulangerie', 'Avenue de la Liberté, Ariana Essoughra', '+216 21 828 202', 4.5, 'Freshly baked bread and pastries daily', 'Freshly baked bread and pastries daily', 'Nourriture de qualité avec un excellent service', 'مطعم يقدم أطباق عالية الجودة', 36.8503, 10.1819, '/uploads/restaurants/restaurant_1099.jpg', '/uploads/restaurants/restaurant_1099_icon.jpg', FALSE, TRUE, FALSE, 3.77, '27-59 min', 0.8800, '08:00', '23:00');

INSERT INTO restaurant_category (restaurant_id, category) VALUES (1099, 'BAKERY');

INSERT INTO menu_category (id, name, name_en, name_fr, name_ar, restaurant_id) VALUES
(1099, 'Bakery Dishes', 'Bakery Dishes', 'Plats bakery', 'أطباق BAKERY', 1099);

-- Menu item: Special Dish 1
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1303, 'Special Dish 1', 'Special Dish 1', 'Plat Spécial 1', 'طبق خاص ١', 'Our signature dish', 'Our signature dish', 'Our signature dish', 'Our signature dish', TRUE, 10.0, TRUE, 1099);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1303, '/uploads/menu-items/item_1303.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1303, 1099);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1606, 1303, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2818, 1606, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2819, 1606, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2820, 1606, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1607, 1303, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2821, 1607, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2822, 1607, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2823, 1607, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 2
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1304, 'Special Dish 2', 'Special Dish 2', 'Plat Spécial 2', 'طبق خاص ٢', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', 'Chef's recommendation', FALSE, 12.0, TRUE, 1099);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1304, '/uploads/menu-items/item_1304.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1304, 1099);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1608, 1304, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2824, 1608, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2825, 1608, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2826, 1608, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1609, 1304, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2827, 1609, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2828, 1609, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2829, 1609, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);

-- Menu item: Special Dish 3
INSERT INTO menu_item (id, name, name_en, name_fr, name_ar, description, description_en, description_fr, description_ar, is_popular, price, available, restaurant_id) VALUES
(1305, 'Special Dish 3', 'Special Dish 3', 'Plat Spécial 3', 'طبق خاص ٣', 'Popular choice', 'Popular choice', 'Popular choice', 'Popular choice', FALSE, 11.0, TRUE, 1099);

INSERT INTO menu_item_images (menu_item_id, image_url) VALUES (1305, '/uploads/menu-items/item_1305.jpg');

INSERT INTO menu_item_categories (menu_item_id, category_id) VALUES (1305, 1099);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1610, 1305, 'Choose your size', 'Choose your size', 'Choisissez votre taille', 'اختر الحجم', 1, 1, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2830, 1610, 'Small', 'Small', 'Petit', 'صغير', 0.0, TRUE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2831, 1610, 'Medium', 'Medium', 'Moyen', 'متوسط', 2.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2832, 1610, 'Large', 'Large', 'Grand', 'كبير', 4.0, FALSE);

INSERT INTO menu_option_group (id, menu_item_id, name, name_en, name_fr, name_ar, min_select, max_select, required) VALUES
(1611, 1305, 'Extra toppings', 'Extra toppings', 'Garnitures supplémentaires', 'إضافات إضافية', 0, 3, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2833, 1611, 'Extra Cheese', 'Extra Cheese', 'Fromage supplémentaire', 'جبنة إضافية', 1.5, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2834, 1611, 'Mushrooms', 'Mushrooms', 'Champignons', 'فطر', 1.0, FALSE);

INSERT INTO menu_item_extra (id, option_group_id, name, name_en, name_fr, name_ar, price, is_default) VALUES
(2835, 1611, 'Olives', 'Olives', 'Olives', 'زيتون', 0.8, FALSE);


-- Update sequences
SELECT setval('restaurant_id_seq', 1100, false);
SELECT setval('menu_item_id_seq', 1306, false);
SELECT setval('menu_category_id_seq', 1100, false);
SELECT setval('menu_option_group_id_seq', 1612, false);
SELECT setval('menu_item_extra_id_seq', 2836, false);