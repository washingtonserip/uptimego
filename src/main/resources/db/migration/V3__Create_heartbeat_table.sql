CREATE TABLE heartbeat (
    id BIGINT PRIMARY KEY,
    uptime_config_id BIGINT NOT NULL,
    status VARCHAR(10),
    response_time DOUBLE PRECISION,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uptime_config_id) REFERENCES uptime_config(id) ON DELETE CASCADE
);