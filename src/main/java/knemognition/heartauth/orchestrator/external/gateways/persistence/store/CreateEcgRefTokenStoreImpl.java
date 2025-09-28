package knemognition.heartauth.orchestrator.external.gateways.persistence.store;


import knemognition.heartauth.orchestrator.external.app.ports.out.CreateEcgRefTokenStore;
import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;
import knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.entity.EcgRefTokenEntity;
import knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.repository.EcgRefTokenRepository;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.EcgRefTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateEcgRefTokenStoreImpl implements CreateEcgRefTokenStore {

    private final EcgRefTokenMapper mapper;
    private final EcgRefTokenRepository deviceCredentialRepository;

    @Override
    @Transactional
    public void create(EcgRefToken toCreate) {
        EcgRefTokenEntity saved = deviceCredentialRepository.save(mapper.toEntity(toCreate));
        mapper.toDomain(saved);
    }

}