CREATE TABLE alerts (
    alert_id BIGINT PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    pulse_id BIGINT REFERENCES pulses(id) ON DELETE CASCADE,
    alert_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'CREATED', -- Example values: 'CREATED', 'SENT', 'ACKNOWLEDGED', 'FAILED'
    acknowledged_at TIMESTAMP -- Optional, if you want to track when the acknowledgment happened
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);
