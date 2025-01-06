CREATE TABLE promotions
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    code           VARCHAR(255)   NOT NULL UNIQUE,
    discount_type  VARCHAR(50)    NOT NULL,
    discount_value DECIMAL(10, 2) NOT NULL,
    is_active      BOOLEAN        NOT NULL,
    start_date     TIMESTAMP      NOT NULL,
    end_date       TIMESTAMP      NOT NULL,
    created_at     TIMESTAMP      NOT NULL,
    updated_at     TIMESTAMP,
    CONSTRAINT CK_DISCOUNT_TYPE CHECK (discount_type IN ('percentage', 'flat_amount'))
);