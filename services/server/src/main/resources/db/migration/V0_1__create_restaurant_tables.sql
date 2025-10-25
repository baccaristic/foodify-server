CREATE TABLE IF NOT EXISTS restaurant (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    rating DOUBLE PRECISION,
    opening_hours VARCHAR(255),
    closing_hours VARCHAR(255),
    description TEXT,
    license_number VARCHAR(100),
    tax_id VARCHAR(100),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    image_url VARCHAR(255),
    icon_url VARCHAR(255),
    top_choice BOOLEAN NOT NULL DEFAULT FALSE,
    free_delivery BOOLEAN NOT NULL DEFAULT FALSE,
    top_eat BOOLEAN NOT NULL DEFAULT FALSE,
    delivery_fee DOUBLE PRECISION,
    delivery_time_range VARCHAR(255),
    restaurant_share_rate NUMERIC(5,4) DEFAULT 0.8800,
    admin_id BIGINT,
    CONSTRAINT fk_restaurant_admin FOREIGN KEY (admin_id) REFERENCES app_users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS restaurant_category (
    restaurant_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    PRIMARY KEY (restaurant_id, category),
    CONSTRAINT fk_restaurant_category_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS restaurant_weekly_operating_hour (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    day_of_week VARCHAR(16) NOT NULL,
    is_open BOOLEAN NOT NULL DEFAULT FALSE,
    opens_at TIME,
    closes_at TIME,
    CONSTRAINT fk_restaurant_weekly_operating_hour_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS restaurant_special_day (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    special_date DATE NOT NULL,
    is_open BOOLEAN NOT NULL DEFAULT FALSE,
    opens_at TIME,
    closes_at TIME,
    CONSTRAINT fk_restaurant_special_day_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT fk_menu_category_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_item (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_popular BOOLEAN NOT NULL DEFAULT FALSE,
    price NUMERIC(10,2) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    promotion_label VARCHAR(255),
    promotion_price NUMERIC(10,2),
    promotion_active BOOLEAN NOT NULL DEFAULT FALSE,
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT fk_menu_item_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_item_images (
    menu_item_id BIGINT NOT NULL,
    image_url VARCHAR(1024) NOT NULL,
    PRIMARY KEY (menu_item_id, image_url),
    CONSTRAINT fk_menu_item_images_item
        FOREIGN KEY (menu_item_id) REFERENCES menu_item (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_item_categories (
    menu_item_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (menu_item_id, category_id),
    CONSTRAINT fk_menu_item_categories_item
        FOREIGN KEY (menu_item_id) REFERENCES menu_item (id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_item_categories_category
        FOREIGN KEY (category_id) REFERENCES menu_category (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_option_group (
    id BIGSERIAL PRIMARY KEY,
    menu_item_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    min_select INTEGER NOT NULL DEFAULT 0,
    max_select INTEGER NOT NULL DEFAULT 0,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_menu_option_group_item
        FOREIGN KEY (menu_item_id) REFERENCES menu_item (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_item_extra (
    id BIGSERIAL PRIMARY KEY,
    option_group_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL DEFAULT 0,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_menu_item_extra_group
        FOREIGN KEY (option_group_id) REFERENCES menu_option_group (id) ON DELETE CASCADE
);
