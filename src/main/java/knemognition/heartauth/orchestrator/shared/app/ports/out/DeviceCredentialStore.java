package knemognition.heartauth.orchestrator.shared.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;

import java.time.Instant;
import java.util.*;

public interface DeviceCredentialStore {
    DeviceCredential create(DeviceCredential toCreate);

    Optional<DeviceCredential> findActiveByDeviceId(String deviceId);

    List<DeviceCredential> listActiveByUserId(UUID userId);

    long countActiveByUserId(UUID userId);

    void updateFcmToken(UUID id, String newFcmToken);

    void touchLastSeen(UUID id, Instant at);

    /** Sets revokedAt if currently active; returns true if something changed. */
    boolean revoke(UUID id, Instant at);

    Optional<DeviceCredential> findById(UUID id);
}
