package knemognition.heartauth.orchestrator.internal.gateways.persistence.jpa.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import knemognition.heartauth.orchestrator.internal.app.ports.out.DeviceDirectory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeviceDirectoryImpl implements DeviceDirectory {

    private final DeviceCredentialRepository deviceCredentialRepository;

    @Override
    public List<String> getActiveFcmTokens(String userId) {
        return deviceCredentialRepository.findActiveFcmTokensByUser(userId);
    }
}
