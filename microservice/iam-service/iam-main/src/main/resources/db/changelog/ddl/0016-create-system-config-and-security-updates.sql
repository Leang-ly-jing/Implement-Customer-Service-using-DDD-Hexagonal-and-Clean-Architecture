CREATE TABLE IF NOT EXISTS system_config (
    id VARCHAR(50) PRIMARY KEY DEFAULT 'DEFAULT',
    password_min_length INT NOT NULL DEFAULT 8,
    require_uppercase BOOLEAN NOT NULL DEFAULT true,
    require_digit BOOLEAN NOT NULL DEFAULT true,
    require_special BOOLEAN NOT NULL DEFAULT true,
    max_failed_attempts INT NOT NULL DEFAULT 5,
    lock_duration_minutes INT NOT NULL DEFAULT 15,
    session_expiration_minutes INT NOT NULL DEFAULT 1440,
    access_token_ttl_minutes INT NOT NULL DEFAULT 60,
    refresh_token_ttl_days INT NOT NULL DEFAULT 30,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100)
);

INSERT INTO system_config (
    id,
    password_min_length,
    require_uppercase,
    require_digit,
    require_special,
    max_failed_attempts,
    lock_duration_minutes,
    session_expiration_minutes,
    access_token_ttl_minutes,
    refresh_token_ttl_days
) VALUES ('DEFAULT', 8, true, true, true, 5, 15, 1440, 60, 30)
ON CONFLICT (id) DO NOTHING;

ALTER TABLE users ADD COLUMN IF NOT EXISTS locked_until TIMESTAMPTZ;

ALTER TABLE user_sessions ADD COLUMN IF NOT EXISTS expires_at TIMESTAMPTZ;
