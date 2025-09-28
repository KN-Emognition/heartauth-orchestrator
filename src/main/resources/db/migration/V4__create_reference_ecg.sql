CREATE TABLE ecg_ref_token
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL,
    ecg_data   JSONB       NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_reference_ecg_user_id ON ecg_ref_token (user_id);

CREATE
    OR REPLACE FUNCTION set_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at
        = now();
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

CREATE TRIGGER trg_reference_ecg_updated_at
    BEFORE UPDATE
    ON ecg_ref_token
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

