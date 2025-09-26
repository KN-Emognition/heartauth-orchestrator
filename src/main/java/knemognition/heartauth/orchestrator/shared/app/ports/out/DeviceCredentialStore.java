package knemognition.heartauth.orchestrator.shared.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;

import java.util.List;
import java.util.UUID;

public interface DeviceCredentialStore {
    List<DeviceCredential> getDeviceCredentials(UUID userId);
}
