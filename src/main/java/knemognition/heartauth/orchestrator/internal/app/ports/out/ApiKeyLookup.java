package knemognition.heartauth.orchestrator.internal.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyLookup {

    Optional<TenantApiKey> findActiveByHash(String keyHash);

    void updateLastUsedAt(UUID apiKeyId, OffsetDateTime when);
}
