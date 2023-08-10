CREATE TABLE heartbeat (
    id UUID PRIMARY KEY,
    uptime_config_id BIGINT NOT NULL,
    status VARCHAR(255),
    response_time DOUBLE PRECISION,
    details JSONB,
    timestamp TIMESTAMP,
    FOREIGN KEY (uptime_config_id) REFERENCES uptime_config(id) ON DELETE CASCADE
);
