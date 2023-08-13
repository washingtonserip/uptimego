CREATE TABLE payments (
    id BIGINT PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    subscription_id BIGINT UNIQUE REFERENCES subscriptions(id) ON DELETE CASCADE
);
