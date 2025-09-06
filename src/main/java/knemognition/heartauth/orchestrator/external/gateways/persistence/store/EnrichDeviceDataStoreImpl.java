package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.external.app.ports.out.EnrichDeviceDataStore;
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
        if (req == null || req.getJti() == null) return;

        pairingStateRepository.findById(req.getJti()).ifPresent(ent -> {
            long now = Instant.now().getEpochSecond();

            if (ent.getExp() != null && ent.getExp() <= now) return;

            enrichDeviceStoreMapper.applyEnrichment(ent, req);

            if (ent.getExp() != null) {
                long remaining = ent.getExp() - now;
                if (remaining <= 0) return;
                ent.setTtlSeconds(remaining);
            }
            pairingStateRepository.save(ent);
        });
    }
}
