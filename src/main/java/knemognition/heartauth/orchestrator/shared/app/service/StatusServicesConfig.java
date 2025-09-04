package knemognition.heartauth.orchestrator.shared.app.service;

import knemognition.heartauth.orchestrator.shared.app.ports.in.StatusService;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.mapper.ResponseStatusMapper;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusServicesConfig {

    @Bean(name = "challengeStatusServiceImpl")
    public StatusService<ChallengeState> challengeStatusService(
            StatusStore<ChallengeState> challengeStateStatusStore,
            ResponseStatusMapper mapper
    ) {
        return new GenericStatusService(challengeStateStatusStore, mapper);
    }

    @Bean(name = "pairingStatusServiceImpl")
    public StatusService<PairingState> pairingStatusService(
            StatusStore<PairingState> pairingStateStatusStore,
            ResponseStatusMapper mapper
    ) {
        return new GenericStatusService<>(pairingStateStatusStore, mapper);
    }
}
