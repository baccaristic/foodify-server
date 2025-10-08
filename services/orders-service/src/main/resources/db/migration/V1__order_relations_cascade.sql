-- Ensure order item foreign key cascades on delete
ALTER TABLE order_item
    DROP CONSTRAINT IF EXISTS fkt4dc2r9nbvbujrljv3e23iibt;
ALTER TABLE order_item
    DROP CONSTRAINT IF EXISTS order_item_order_id_fkey;
ALTER TABLE order_item
    ADD CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
            ON DELETE CASCADE;

-- Ensure delivery foreign key cascades on delete
ALTER TABLE delivery
    DROP CONSTRAINT IF EXISTS fk6r671c5p1g4f0r2kf1v0f7d4l;
ALTER TABLE delivery
    DROP CONSTRAINT IF EXISTS delivery_order_id_fkey;
ALTER TABLE delivery
    ADD CONSTRAINT fk_delivery_order
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
            ON DELETE CASCADE;

-- Ensure order status history foreign key cascades on delete
ALTER TABLE order_status_history
    DROP CONSTRAINT IF EXISTS fk2inkbwoey17kd75dbf8e7ihg0;
ALTER TABLE order_status_history
    DROP CONSTRAINT IF EXISTS order_status_history_order_id_fkey;
ALTER TABLE order_status_history
    ADD CONSTRAINT fk_order_status_history_order
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
            ON DELETE CASCADE;
