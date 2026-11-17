UPDATE login_history SET failure_reason = 'ACCOUNT_DISABLED' WHERE failure_reason ILIKE '%disabled%';
UPDATE login_history SET failure_reason = 'BAD_CREDENTIALS' WHERE failure_reason ILIKE '%bad credential%' OR failure_reason ILIKE '%password%';
UPDATE login_history SET failure_reason = 'ACCOUNT_LOCKED' WHERE failure_reason ILIKE '%locked%';
UPDATE login_history SET failure_reason = 'UNKNOWN' WHERE failure_reason IS NOT NULL AND failure_reason NOT IN ('BAD_CREDENTIALS', 'ACCOUNT_LOCKED', 'ACCOUNT_DISABLED', 'USER_NOT_FOUND', 'UNKNOWN');
