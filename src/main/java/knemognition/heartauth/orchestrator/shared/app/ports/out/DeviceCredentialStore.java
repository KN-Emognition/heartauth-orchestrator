package knemognition.heartauth.orchestrator.shared.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;

import java.time.Instant;
import java.util.*;

public interface DeviceCredentialStore {
    DeviceCredential create(DeviceCredential toCreate);

    List<String> getActiveFcmTokens(UUID userId);
}
