package knemognition.heartauth.orchestrator.pairings.infrastructure;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.pairings.api.DeviceEnrichException;
import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.domain.*;
import knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.entity.PairingStateRedis;
import knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.mappers.PairingStoreMapper;
import knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class PairingStoreImpl implements PairingStore {

    private final PairingStateRepository repository;
    private final PairingStoreMapper mapper;
    private static final EnumSet<FlowStatus> TERMINAL_STATUSES = EnumSet.of(FlowStatus.APPROVED, FlowStatus.DENIED,
            FlowStatus.EXPIRED);

    @Override
    public CreatedPairingResult createPairing(CreatePairing state) {
        boolean recreated = denyPreviousActivePairing(state.getTenantId(), state.getUserId());

        PairingStateRedis ent = mapper.fromCreate(state);
        ent.setReason(recreated ? FlowStatusReason.FLOW_RECREATED : FlowStatusReason.FLOW_CREATED);

        repository.save(ent);
        return mapper.toResult(ent);
    }

    @Override
    public Optional<PairingState> getFlow(UUID id) {
        return repository.findById(id)
                .map(ent -> {
                    long now = Instant.now()
                            .getEpochSecond();
                    if (ent.getExp() != null && ent.getExp() <= now) return null;
                    return mapper.toDomain(ent);
                });
    }

    @Override
    @Transactional
    public void enrichWithDeviceData(EnrichDeviceData req) {
        if (req == null) {
            throw new DeviceEnrichException("enrich_request_missing");
        }
        if (req.getJti() == null) {
            throw new DeviceEnrichException("jti_missing");
        }

        var ent = repository.findById(req.getJti())
                .orElseThrow(() -> new DeviceEnrichException("pairing_not_found_or_expired"));

        long now = Instant.now()
                .getEpochSecond();
        Long exp = ent.getExp();

        if (exp != null && exp <= now) {
            throw new DeviceEnrichException("pairing_not_found_or_expired");
        }
        mapper.applyEnrichment(ent, req);
        if (exp != null) {
            long remaining = exp - now;
            if (remaining <= 0) {
                throw new DeviceEnrichException("pairing_not_found_or_expired");
            }
            ent.setTtlSeconds(remaining);
        }

        repository.save(ent);
    }

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return repository.findById(statusChange.getId())
                .map(ent -> {
                    mapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    repository.save(ent);
                    return true;
                })
                .orElse(false);
    }


    private boolean denyPreviousActivePairing(UUID tenantId, UUID userId) {
        List<PairingStateRedis> actives = repository.findAllByTenantIdAndUserIdOrderByCreatedAtDesc(
                        tenantId, userId)
                .stream()
                .filter(this::isActive)
                .toList();

        for (PairingStateRedis existing : actives) {
            existing.setStatus(FlowStatus.DENIED);
            existing.setReason(FlowStatusReason.FLOW_DENIED_BY_RECREATING_FLOW);
            repository.save(existing);
        }

        return !actives.isEmpty();
    }

    private boolean isActive(PairingStateRedis state) {
        FlowStatus status = state.getStatus();
        if (status == null || TERMINAL_STATUSES.contains(status)) {
            return false;
        }
        Long exp = state.getExp();
        return exp == null || exp > Instant.now()
                .getEpochSecond();
    }
}
