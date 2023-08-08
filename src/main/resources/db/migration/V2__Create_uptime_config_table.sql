CREATE TABLE uptime_config (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    url VARCHAR(255),
    type VARCHAR(255),
    options JSONB,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
