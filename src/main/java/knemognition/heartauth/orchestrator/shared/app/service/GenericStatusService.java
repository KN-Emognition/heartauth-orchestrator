package knemognition.heartauth.orchestrator.shared.app.service;

import knemognition.heartauth.orchestrator.shared.app.ports.in.StatusService;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.app.mapper.ResponseStatusMapper;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record GenericStatusService<T>(
        StatusStore<T> statusStore,
        ResponseStatusMapper responseStatusMapper) implements StatusService {


    @Override
    public StatusResponse status(UUID id) {
        StatusResponse response = statusStore.getStatus(id)
                .map(responseStatusMapper::map)
                .orElseGet(responseStatusMapper::notFound);

        log.info("Queried status for {}", id);
        return response;
    }

    @Override
    public boolean setStatus(StatusChange statusChange) {
        boolean change = statusStore.setStatus(statusChange);
        if (change) {
            log.info("Changed status for {}", statusChange.getId());
        }
        return change;
    }


}
