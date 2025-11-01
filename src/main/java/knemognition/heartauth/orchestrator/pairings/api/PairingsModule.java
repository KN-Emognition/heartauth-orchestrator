package knemognition.heartauth.orchestrator.pairings.api;

import org.springframework.modulith.NamedInterface;

@NamedInterface
public interface PairingsModule {
    PairingStatusRead getStatus(GetPairingStatusCmd cmd);

    CreatedPairingRead create(CreatePairingCmd req);

    InitPairingRead init(InitPairingCmd cmd);

    void complete(CompletePairingCmd cmd);
}
