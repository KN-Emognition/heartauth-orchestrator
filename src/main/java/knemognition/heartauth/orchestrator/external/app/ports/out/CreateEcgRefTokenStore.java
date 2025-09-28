package knemognition.heartauth.orchestrator.external.app.ports.out;


import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;

public interface CreateEcgRefTokenStore {
    void create(EcgRefToken toCreate);

}
