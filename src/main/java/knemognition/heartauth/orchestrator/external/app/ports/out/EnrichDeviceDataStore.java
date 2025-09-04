package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;

public interface EnrichDeviceDataStore {
    void enrich(EnrichDeviceData req);
}
