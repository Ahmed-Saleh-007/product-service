ALTER TABLE orders
    ADD COLUMN discount_amount DECIMAL(10, 2);

ALTER TABLE orders
    ADD COLUMN total_amount_after_discount DECIMAL(10, 2);

ALTER TABLE orders
    ADD COLUMN promotion_id INT;

ALTER TABLE orders
    ADD CONSTRAINT fk_order_promotion
        FOREIGN KEY (promotion_id) REFERENCES promotions (id);