package knemognition.heartauth.orchestrator.internal.app.service;


import knemognition.heartauth.orchestrator.internal.app.mapper.ResponseStatusMapper;
import knemognition.heartauth.orchestrator.internal.app.service.status.GenericStatusService;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import org.springframework.stereotype.Service;

@Service
public class PairingStatusServiceImpl extends GenericStatusService<PairingState> {

    public PairingStatusServiceImpl(
            StatusStore<PairingState> pairingStateStatusStore,
            ResponseStatusMapper mapper
    ) {
        super(pairingStateStatusStore, mapper);
    }
}
