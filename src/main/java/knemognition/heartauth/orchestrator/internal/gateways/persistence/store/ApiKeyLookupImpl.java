package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.internal.app.ports.out.ApiKeyLookup;
import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.TenantApiKeyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApiKeyLookupImpl implements ApiKeyLookup {
    private final TenantApiKeyRepository repo;
    private final TenantApiKeyMapper mapper;

    @Override
    public Optional<TenantApiKey> findActiveByHash(String keyHash) {
        return repo.findActiveByHash(keyHash)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void updateLastUsedAt(UUID apiKeyId, OffsetDateTime when) {
        TenantApiKeyEntity key = repo.getReferenceById(apiKeyId);
        key.setLastUsedAt(when);
        repo.save(key);
    }
}
