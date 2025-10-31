package knemognition.heartauth.orchestrator.internal.app.ports.out;

import knemognition.heartauth.orchestrator.user.domain.Device;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InternalMainStore {

    Optional<UUID> getTenantIdForActiveKeyHash(String keyHash);

    boolean checkIfUserExists(IdentifiableUserCmd data);

    List<Device> findDevices(IdentifiableUserCmd data);
}
