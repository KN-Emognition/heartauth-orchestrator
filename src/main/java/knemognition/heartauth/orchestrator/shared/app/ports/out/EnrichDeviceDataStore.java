package knemognition.heartauth.orchestrator.shared.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.EnrichDeviceData;

public interface EnrichDeviceDataStore {
    void enrich(EnrichDeviceData req);
}
