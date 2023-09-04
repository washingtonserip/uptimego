CREATE TABLE channels (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    channel_type VARCHAR(50) NOT NULL, -- Example values: 'Whatsapp', 'Telegram', 'Email', 'Phone', 'Slack'
    metadata JSONB,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
