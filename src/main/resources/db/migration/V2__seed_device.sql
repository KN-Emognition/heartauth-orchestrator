-- Seed a single active device (static values, edit if needed).
-- Idempotent: UPDATE first; if no row was updated, INSERT.

WITH updated AS (
UPDATE device_credential
SET fcm_token      = 'dev-fcm-token-123',
    display_name   = 'Pixel 8 Pro (seed)',
    public_key_pem = '-----BEGIN PUBLIC KEY-----
PLACEHOLDER
-----END PUBLIC KEY-----',
    platform       = 'ANDROID',
    last_seen_at   = now(),
    updated_at     = now()
WHERE user_id  = '1a2b3c-user'
  AND device_id = 'android:3f24a1c2'
  AND revoked_at IS NULL
    RETURNING 1
)
INSERT INTO device_credential (
    user_id, device_id, display_name, public_key_pem, fcm_token, platform, last_seen_at
)
SELECT
    '1a2b3c-user',
    'android:3f24a1c2',
    'Pixel 8 Pro (seed)',
    '-----BEGIN PUBLIC KEY-----
  PLACEHOLDER
  -----END PUBLIC KEY-----',
    'dev-fcm-token-123',
    'ANDROID',
    now()
    WHERE NOT EXISTS (SELECT 1 FROM updated);
