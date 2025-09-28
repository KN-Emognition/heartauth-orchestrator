package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetEcgRefTokenStore {
    Optional<List<EcgRefToken>> getRefToken(UUID userId);
}
