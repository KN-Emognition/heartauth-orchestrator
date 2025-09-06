package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.ports.out.FindFcmTokensStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.DeviceCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindFcmTokensStoreImpl implements FindFcmTokensStore {

    private final DeviceCredentialRepository deviceCredentialRepository;


    @Override
    public List<String> getActiveFcmTokens(UUID userId) {
        return deviceCredentialRepository.findActiveFcmTokensByUser(userId);
    }
}