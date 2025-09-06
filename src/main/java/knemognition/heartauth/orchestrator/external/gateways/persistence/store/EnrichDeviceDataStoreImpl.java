package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.external.app.ports.out.EnrichDeviceDataStore;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.DeviceEnrichException;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.EnrichDeviceStoreMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;


@Repository
@RequiredArgsConstructor
public class EnrichDeviceDataStoreImpl implements EnrichDeviceDataStore {


    private final PairingStateRepository pairingStateRepository;
    private final EnrichDeviceStoreMapper enrichDeviceStoreMapper;


    @Override
    @Transactional
    public void enrich(EnrichDeviceData req) {
        if (req == null) {
            throw new DeviceEnrichException("enrich_request_missing");
        }
        if (req.getJti() == null) {
            throw new DeviceEnrichException("jti_missing");
        }

        var ent = pairingStateRepository.findById(req.getJti())
                .orElseThrow(() -> new DeviceEnrichException("pairing_not_found_or_expired"));

        long now = Instant.now().getEpochSecond();
        Long exp = ent.getExp();

        if (exp != null && exp <= now) {
            throw new DeviceEnrichException("pairing_not_found_or_expired");
        }
        enrichDeviceStoreMapper.applyEnrichment(ent, req);
        if (exp != null) {
            long remaining = exp - now;
            if (remaining <= 0) {
                throw new DeviceEnrichException("pairing_not_found_or_expired");
            }
            ent.setTtlSeconds(remaining);
        }

        pairingStateRepository.save(ent);
    }
}
