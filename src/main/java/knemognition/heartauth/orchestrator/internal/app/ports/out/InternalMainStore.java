package knemognition.heartauth.orchestrator.internal.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;
import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InternalMainStore {

    Optional<TenantApiKey> findActiveByHash(String keyHash);

    void updateLastUsedAt(UUID apiKeyId, OffsetDateTime when);

    boolean checkIfUserExists(IdentifiableUser data);

    List<Device> findDevices(IdentifiableUser data);
}
