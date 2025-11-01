package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.pairings.api.GetPairingStatusCmd;
import knemognition.heartauth.orchestrator.pairings.api.PairingStatusRead;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPairingStatusHandler {
    private final PairingStore pairingStore;
    private final PairingsMapper pairingsMapper;

    public PairingStatusRead handle(GetPairingStatusCmd cmd) {
        Optional<PairingState> state = pairingStore.getFlow(cmd.getChallengeId());
        log.info("[PAIRING] Queried state for {}", cmd.getChallengeId());

        if (state.isEmpty() || !cmd.getTenantId()
                .equals(state.get()
                        .getTenantId())) {
            log.info("[PAIRING] Tenant can't access this state");
            return PairingStatusRead.builder()
                    .status(FlowStatus.NOT_FOUND)
                    .build();
        }

        return pairingsMapper.toRead(state.get());
    }
}
