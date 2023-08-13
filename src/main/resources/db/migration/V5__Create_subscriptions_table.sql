CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    plan_id BIGINT REFERENCES plans(id) ON DELETE CASCADE,
    UNIQUE(user_id)   -- Assuming one subscription per user
);
