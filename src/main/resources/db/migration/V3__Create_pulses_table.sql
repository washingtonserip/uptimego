CREATE TABLE pulses (
    id BIGINT PRIMARY KEY,
    uptime_config_id BIGINT NOT NULL,
    status VARCHAR(10),
    latency INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uptime_config_id) REFERENCES uptime_config(id) ON DELETE CASCADE
);
