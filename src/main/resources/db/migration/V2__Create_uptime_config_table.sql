CREATE TABLE uptime_config (
    id BIGINT PRIMARY KEY,
    user_id UUID NOT NULL,
    url VARCHAR(255),
    type VARCHAR(255),
    options JSONB,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_unique_user_url_type
ON uptime_config (user_id, url, type);
