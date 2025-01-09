-- Insert a row with a PERCENTAGE discount type
INSERT INTO promotions (code, discount_type, discount_value, is_active, start_date, end_date, created_at, updated_at)
VALUES ('NEWYEAR25', 'PERCENTAGE', 10.00, TRUE, '2025-01-01 00:00:00', '2025-01-31 23:59:59', '2025-01-01 00:00:00', '2025-01-01 00:00:00');

-- Insert a row with a FLAT_AMOUNT discount type
INSERT INTO promotions (code, discount_type, discount_value, is_active, start_date, end_date, created_at, updated_at)
VALUES ('PROMO100', 'FLAT_AMOUNT', 100.00, TRUE, '2025-01-01 00:00:00', '2025-06-30 23:59:59', '2025-01-01 00:00:00',
        '2025-01-01 00:00:00');
