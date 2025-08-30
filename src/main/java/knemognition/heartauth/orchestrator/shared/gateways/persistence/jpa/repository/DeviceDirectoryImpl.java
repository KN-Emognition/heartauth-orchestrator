package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import knemognition.heartauth.orchestrator.internal.app.ports.out.DeviceDirectory;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeviceDirectoryImpl implements DeviceDirectory {

    private final DeviceCredentialRepository deviceCredentialRepository;

    @Override
    public List<String> getActiveFcmTokens(UUID userId) {
        return deviceCredentialRepository.findActiveFcmTokensByUser(userId);
    }
}
