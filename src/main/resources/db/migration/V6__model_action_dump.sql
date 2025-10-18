CREATE TABLE model_action_store
(
    id             UUID PRIMARY KEY,
    correlation_id UUID        NOT NULL,
    payload        TEXT        NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
