CREATE TABLE IF NOT EXISTS user_sessions (
                                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_uuid UUID NOT NULL,
    device_type VARCHAR(50),
    browser VARCHAR(150),
    ip_address VARCHAR(50),
    location VARCHAR(100),
    revoked_at TIMESTAMPTZ,
    last_active_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_user_sessions_user_uuid ON user_sessions(user_uuid);
CREATE INDEX IF NOT EXISTS idx_user_sessions_active ON user_sessions(user_uuid, revoked_at);