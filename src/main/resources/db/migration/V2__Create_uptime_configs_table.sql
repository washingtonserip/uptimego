CREATE TABLE uptime_configs (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    url VARCHAR(1000),
    type VARCHAR(10),
    options JSONB,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_unique_user_url_type
ON uptime_configs (user_id, url, type);