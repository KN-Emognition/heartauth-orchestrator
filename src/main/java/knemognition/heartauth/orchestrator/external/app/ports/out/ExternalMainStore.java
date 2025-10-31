package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.user.domain.Device;
import knemognition.heartauth.orchestrator.user.domain.EcgRefData;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;

import java.util.Optional;

public interface ExternalMainStore {

    Optional<EcgRefData> findRefData(IdentifiableUserCmd identUser);

    void savePairingArtifacts(EcgRefData ref, Device device, IdentifiableUserCmd user);
}
