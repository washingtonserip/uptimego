CREATE TABLE pulses (
    id BIGINT PRIMARY KEY,
    target_id BIGINT REFERENCES targets(id) ON DELETE CASCADE,
    status VARCHAR(20),
    latency INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
