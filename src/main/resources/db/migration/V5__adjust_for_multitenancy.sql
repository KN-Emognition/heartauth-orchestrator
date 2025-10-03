CREATE
EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE tenants
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    tenant_id UUID        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_tenants_id UNIQUE (tenant_id)
);

CREATE TABLE tenant_api_keys
(
    id           UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    tenants_fk   UUID        NOT NULL REFERENCES tenants (id) ON DELETE CASCADE,
    key_hash     TEXT        NOT NULL, -- e.g. base64(sha256(key+salt))
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_used_at TIMESTAMPTZ,
    expires_at   TIMESTAMPTZ,
    revoked_at   TIMESTAMPTZ,
    CONSTRAINT uq_key_per_tenant UNIQUE (tenants_fk, key_hash)
);

CREATE TABLE app_user
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    tenants_fk UUID        NOT NULL REFERENCES tenants (id) ON DELETE CASCADE,
    user_id    UUID        NOT NULL, -- Keycloak user ID
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_user_per_tenant UNIQUE (tenants_fk, user_id)
);

DO
$$
BEGIN
    IF
NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'platform_enum') THEN
CREATE TYPE platform_enum AS ENUM ('ANDROID', 'IOS');
END IF;
END$$;

CREATE TABLE user_device
(
    id             UUID PRIMARY KEY       DEFAULT gen_random_uuid(),
    app_user_fk    UUID          NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    device_id      TEXT          NOT NULL,
    display_name   TEXT          NOT NULL,
    public_key_pem TEXT          NOT NULL,
    fcm_token      TEXT,
    platform       platform_enum NOT NULL,
    os_version     TEXT,
    model          TEXT,
    created_at     TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ   NOT NULL DEFAULT now(),
    last_seen_at   TIMESTAMPTZ,
    revoked_at     TIMESTAMPTZ,
    CONSTRAINT uq_device_per_user UNIQUE (app_user_fk, device_id)
);

CREATE TABLE ecg_ref_data
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    app_user_fk UUID        NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    ecg_data    JSONB       NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_ecg_per_user UNIQUE (app_user_fk)
);

CREATE
OR REPLACE FUNCTION set_updated_at() RETURNS trigger AS $$
BEGIN
  NEW.updated_at
:= now();
RETURN NEW;
END$$
LANGUAGE plpgsql;

CREATE TRIGGER trg_tenants_updated
    BEFORE UPDATE
    ON tenants
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_tenant_api_keys_updated
    BEFORE UPDATE
    ON tenant_api_keys
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_app_user_updated
    BEFORE UPDATE
    ON app_user
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_device_credential_updated
    BEFORE UPDATE
    ON user_device
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_ecg_ref_data_updated
    BEFORE UPDATE
    ON ecg_ref_data
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

