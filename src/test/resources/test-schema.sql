DROP TABLE IF EXISTS tenant_api_keys;
DROP TABLE IF EXISTS tenants;

CREATE TABLE tenants
(
    id         UUID      NOT NULL PRIMARY KEY,
    tenant_id  UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_tenant_id UNIQUE (tenant_id)
);

CREATE TABLE tenant_api_keys
(
    id         UUID      NOT NULL PRIMARY KEY,
    tenants_fk UUID      NOT NULL,
    key_hash   TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tenant_api_keys_tenants
        FOREIGN KEY (tenants_fk) REFERENCES tenants (id) ON DELETE CASCADE,
    CONSTRAINT uq_key_per_tenant UNIQUE (tenants_fk, key_hash)
);
