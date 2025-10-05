package knemognition.heartauth.orchestrator.internal.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InternalMainStore {

    Optional<UUID> getTenantIdForActiveKeyHash(String keyHash);

    boolean checkIfUserExists(IdentifiableUser data);

    List<Device> findDevices(IdentifiableUser data);
}
