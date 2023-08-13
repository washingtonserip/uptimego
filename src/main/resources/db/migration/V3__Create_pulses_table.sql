CREATE TABLE pulses (
    id BIGINT PRIMARY KEY,
    uptime_config_id BIGINT REFERENCES uptime_configs(id) ON DELETE CASCADE,
    status VARCHAR(10),
    latency INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
