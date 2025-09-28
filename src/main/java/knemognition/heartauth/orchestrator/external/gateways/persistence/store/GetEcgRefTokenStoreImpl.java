package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;
import knemognition.heartauth.orchestrator.external.app.ports.out.GetEcgRefTokenStore;
import knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.repository.EcgRefTokenRepository;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.EcgRefTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GetEcgRefTokenStoreImpl implements GetEcgRefTokenStore {

    private final EcgRefTokenRepository ecgRefTokenRepository;
    private final EcgRefTokenMapper getEcgRefTokenStoreMapper;

    @Override
    public Optional<List<EcgRefToken>> getRefToken(UUID userId) {
        return ecgRefTokenRepository.findAllByUserId(userId).map(ent -> ent.stream().map(getEcgRefTokenStoreMapper::toDomain).toList());
    }
}
