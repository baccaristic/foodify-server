CREATE TABLE IF NOT EXISTS client_favorite_restaurants (
    client_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    PRIMARY KEY (client_id, restaurant_id),
    CONSTRAINT fk_client_favorite_restaurants_client
        FOREIGN KEY (client_id) REFERENCES app_users (id) ON DELETE CASCADE,
    CONSTRAINT fk_client_favorite_restaurants_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS client_favorite_menu_items (
    client_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    PRIMARY KEY (client_id, menu_item_id),
    CONSTRAINT fk_client_favorite_menu_items_client
        FOREIGN KEY (client_id) REFERENCES app_users (id) ON DELETE CASCADE,
    CONSTRAINT fk_client_favorite_menu_items_menu_item
        FOREIGN KEY (menu_item_id) REFERENCES menu_item (id) ON DELETE CASCADE
);
