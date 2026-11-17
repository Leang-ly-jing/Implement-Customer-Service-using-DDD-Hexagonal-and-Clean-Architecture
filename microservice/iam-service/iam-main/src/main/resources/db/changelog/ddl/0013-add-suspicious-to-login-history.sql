ALTER TABLE login_history
    ADD COLUMN IF NOT EXISTS is_suspicious BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS suspicious_reason VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_login_history_ip_login_at ON login_history(ip_address, login_at DESC);
CREATE INDEX IF NOT EXISTS idx_login_history_username_login_at ON login_history(username, login_at DESC);
CREATE INDEX IF NOT EXISTS idx_login_history_suspicious_login_at ON login_history(is_suspicious, login_at DESC);
CREATE INDEX IF NOT EXISTS idx_login_history_status_login_at ON login_history(status, login_at DESC);
