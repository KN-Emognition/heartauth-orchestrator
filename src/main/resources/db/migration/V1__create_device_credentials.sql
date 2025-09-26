CREATE TABLE device_credential
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    user_id        UUID        NOT NULL,
    device_id      TEXT        NOT NULL,
    display_name   TEXT        NOT NULL,
    public_key_pem TEXT        NOT NULL,
    fcm_token      TEXT        NOT NULL,

    platform       TEXT        NOT NULL CHECK (platform IN ('ANDROID', 'IOS')),
    os_version     TEXT,
    model          TEXT,

    attestation    JSONB,       -- raw attestation payload (optional)

    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_seen_at   TIMESTAMPTZ, -- updated on app heartbeats / challenge complete
    revoked_at     TIMESTAMPTZ  -- null => active

);

CREATE UNIQUE INDEX uq_device_credential_device_id_active
    ON device_credential (device_id) WHERE revoked_at IS NULL;

CREATE UNIQUE INDEX uq_device_credential_user_device_active
    ON device_credential (user_id, device_id) WHERE revoked_at IS NULL;

CREATE INDEX ix_device_credential_user_active
    ON device_credential (user_id) WHERE revoked_at IS NULL;


CREATE
OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at
= now();
RETURN NEW;
END; $$
LANGUAGE plpgsql;

CREATE TRIGGER trg_device_credential_updated_at
    BEFORE UPDATE
    ON device_credential
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
