package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.store;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.EnrichDeviceDataStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper.PairingStateRedisMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PairingStoreImpl
        implements StatusStore<PairingState>, CreateFlowStore<CreatePairing>, GetFlowStore<PairingState>, EnrichDeviceDataStore {

    private static final long DEFAULT_TTL_SECONDS = 300L;

    private final PairingStateRepository repo;
    private final PairingStateRedisMapper mapper;

    @Override
    public CreatedFlowResult create(CreatePairing state) {
        final UUID id = state.getJti();
        final long ttl = Optional.ofNullable(state.getTtlSeconds()).filter(t -> t > 0).orElse(DEFAULT_TTL_SECONDS);
        var ent = mapper.fromCreate(state, id, ttl);
        repo.save(ent);
        return mapper.toCreatedResult(ent);
    }

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return repo.findById(statusChange.getId())
                .map(ent -> {
                    mapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    repo.save(ent);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<FlowStatusDescription> getStatus(UUID id) {
        return repo.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;
            return mapper.toStatus(ent);
        });
    }

    @Override
    public Optional<PairingState> getFlow(UUID id) {
        return repo.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;
            return mapper.toDomain(ent);
        });
    }

    @Override
    @Transactional
    public void enrich(EnrichDeviceData req) {
        if (req == null || req.getJti() == null) return;

        repo.findById(req.getJti()).ifPresent(ent -> {
            long now = Instant.now().getEpochSecond();

            if (ent.getExp() != null && ent.getExp() <= now) return;

            mapper.applyEnrichment(ent, req);

            if (ent.getExp() != null) {
                long remaining = ent.getExp() - now;
                if (remaining <= 0) return;
                ent.setTtlSeconds(remaining);
            }
            repo.save(ent);
        });
    }
}
