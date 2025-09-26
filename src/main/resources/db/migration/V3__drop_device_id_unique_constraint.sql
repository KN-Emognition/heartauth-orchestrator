DROP INDEX IF EXISTS uq_device_credential_device_id_active;
DROP INDEX IF EXISTS uq_device_credential_user_device_active;

CREATE INDEX IF NOT EXISTS ix_device_credential_device_active
    ON device_credential (device_id) WHERE revoked_at IS NULL;
