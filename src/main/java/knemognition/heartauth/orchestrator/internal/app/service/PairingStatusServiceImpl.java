package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.mapper.PairingStatusMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.StatusService;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.ports.out.PairingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PairingStatusServiceImpl implements StatusService {
    private final PairingStore store;
    private final PairingStatusMapper mapper;

    @Override
    public StatusResponse status(UUID challengeId) {
        StatusResponse response = store.get(challengeId)
                .map(mapper::toResponse)
                .orElseGet(mapper::notFound);

        log.info("Queried status for pairing {}", challengeId);
        return response;
    }
}
