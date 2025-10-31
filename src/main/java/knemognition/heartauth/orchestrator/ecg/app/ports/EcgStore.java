package knemognition.heartauth.orchestrator.ecg.app.ports;


import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;

import java.util.Optional;
import java.util.UUID;

public interface EcgStore {
    Optional<RefEcg> getReferenceEcg(UUID userId);

    void saveReferenceEcg(UUID userId, RefEcg refEcg);
}
