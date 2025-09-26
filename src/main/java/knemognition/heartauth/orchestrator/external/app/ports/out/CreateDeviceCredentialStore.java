package knemognition.heartauth.orchestrator.external.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;

public interface CreateDeviceCredentialStore {
    void create(DeviceCredential toCreate);

}
