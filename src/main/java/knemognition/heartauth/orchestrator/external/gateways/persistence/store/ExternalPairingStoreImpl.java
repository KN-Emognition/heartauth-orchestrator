package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalPairingStore;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.DeviceEnrichException;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.ExternalPairingStoreMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
@RequiredArgsConstructor
public class ExternalPairingStoreImpl implements ExternalPairingStore {

    private final PairingStateRepository pairingStateRepository;
    private final ExternalPairingStoreMapper externalPairingStoreMapper;

    @Override
    @Transactional
    public void enrichWithDeviceData(EnrichDeviceData req) {
        if (req == null) {
            throw new DeviceEnrichException("enrich_request_missing");
        }
        if (req.getJti() == null) {
            throw new DeviceEnrichException("jti_missing");
        }

        var ent = pairingStateRepository.findById(req.getJti())
                .orElseThrow(() -> new DeviceEnrichException("pairing_not_found_or_expired"));

        long now = Instant.now()
                .getEpochSecond();
        Long exp = ent.getExp();

        if (exp != null && exp <= now) {
            throw new DeviceEnrichException("pairing_not_found_or_expired");
        }
        externalPairingStoreMapper.applyEnrichment(ent, req);
        if (exp != null) {
            long remaining = exp - now;
            if (remaining <= 0) {
                throw new DeviceEnrichException("pairing_not_found_or_expired");
            }
            ent.setTtlSeconds(remaining);
        }

        pairingStateRepository.save(ent);
    }

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return pairingStateRepository.findById(statusChange.getId())
                .map(ent -> {
                    externalPairingStoreMapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    pairingStateRepository.save(ent);
                    return true;
                })
                .orElse(false);
    }
}
