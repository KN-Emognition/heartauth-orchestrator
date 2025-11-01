package knemognition.heartauth.orchestrator.pairings.app;

import knemognition.heartauth.orchestrator.pairings.api.*;
import knemognition.heartauth.orchestrator.pairings.app.handlers.CompletePairingHandler;
import knemognition.heartauth.orchestrator.pairings.app.handlers.CreatePairingHandler;
import knemognition.heartauth.orchestrator.pairings.app.handlers.GetPairingStatusHandler;
import knemognition.heartauth.orchestrator.pairings.app.handlers.InitPairingHandler;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(PairingProperties.class)
public class PairingsModuleImpl implements PairingsModule {
    private final GetPairingStatusHandler getPairingStatusHandler;
    private final CreatePairingHandler createPairingHandler;
    private final InitPairingHandler initPairingHandler;
    private final CompletePairingHandler completePairingHandler;

    @Override
    public PairingStatusRead getStatus(GetPairingStatusCmd cmd) {
        return getPairingStatusHandler.handle(cmd);
    }

    @Override
    public CreatedPairingRead create(CreatePairingCmd cmd) {
        return createPairingHandler.createPairing(cmd);
    }

    @Override
    public InitPairingRead init(InitPairingCmd cmd) {
        return initPairingHandler.handle(cmd);
    }

    @Override
    public void complete(CompletePairingCmd cmd) {
        completePairingHandler.handle(cmd);
    }

}
