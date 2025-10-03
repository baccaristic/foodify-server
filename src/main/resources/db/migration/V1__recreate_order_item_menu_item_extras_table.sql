-- Ensure the order_item_menu_item_extras join table uses the correct column name
DROP TABLE IF EXISTS order_item_menu_item_extras;

CREATE TABLE order_item_menu_item_extras (
    order_item_id BIGINT NOT NULL,
    menu_item_extras_id BIGINT NOT NULL
);

ALTER TABLE order_item_menu_item_extras
    ADD CONSTRAINT fk_order_item_menu_item_extras_order FOREIGN KEY (order_item_id) REFERENCES order_item(id);
ALTER TABLE order_item_menu_item_extras
    ADD CONSTRAINT fk_order_item_menu_item_extras_extra FOREIGN KEY (menu_item_extras_id) REFERENCES menu_item_extra(id);
