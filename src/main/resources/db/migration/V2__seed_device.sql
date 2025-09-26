WITH p
         AS (SELECT '00000000-0000-0000-0000-000000000000'::uuid AS user_id, 'android:3f24a1c2'::text                    AS device_id, 'Pixel 8 Pro (seed)'::text                  AS display_name, '-----BEGIN PUBLIC KEY-----MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE8/2k2MUkTlixFvR9ooUgBOwKgj5vBMVf3NGFOgV3TJcwaZme8fTcNjPxSmSrMoPd3LS6OJ+IMzNkQO5CVI79Vw==-----END PUBLIC KEY-----'::text AS public_key_pem, 'dev-fcm-token-123'::text                   AS fcm_token, 'ANDROID'::text                             AS platform, now() AS ts)
INSERT
INTO device_credential (
  user_id, device_id, display_name, public_key_pem, fcm_token, platform, last_seen_at
)
SELECT p.user_id, p.device_id, p.display_name, p.public_key_pem, p.fcm_token, p.platform, p.ts
FROM p ON CONFLICT (user_id, device_id)
WHERE revoked_at IS NULL
    DO
UPDATE SET
    display_name = EXCLUDED.display_name,
    public_key_pem = EXCLUDED.public_key_pem,
    fcm_token = EXCLUDED.fcm_token,
    platform = EXCLUDED.platform,
    last_seen_at = EXCLUDED.last_seen_at;