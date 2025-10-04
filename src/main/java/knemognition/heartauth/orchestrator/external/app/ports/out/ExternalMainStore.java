package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgRefData;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;

import java.util.Optional;

public interface ExternalMainStore {

    Optional<EcgRefData> findRefData(IdentifiableUser identUser);

    void savePairingArtifacts(EcgRefData ref, Device device, IdentifiableUser user);
}
