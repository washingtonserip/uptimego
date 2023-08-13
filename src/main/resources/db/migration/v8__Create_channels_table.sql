CREATE TABLE channels (
    channel_id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    channel_type VARCHAR(50) NOT NULL, -- Example values: 'Whatsapp', 'Telegram', 'Email', 'Phone', 'Slack'
    metadata JSONB, -- Storing channel-specific metadata
);
