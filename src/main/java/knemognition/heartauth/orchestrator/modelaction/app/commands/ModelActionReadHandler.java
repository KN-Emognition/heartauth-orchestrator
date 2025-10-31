package knemognition.heartauth.orchestrator.modelaction.app.commands;

import knemognition.heartauth.orchestrator.modelaction.api.ModelActionRead;
import knemognition.heartauth.orchestrator.modelaction.app.mappers.ModelActionMapper;
import knemognition.heartauth.orchestrator.modelaction.app.ports.ModelActionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelActionReadHandler {

    private final ModelActionStore store;
    private final ModelActionMapper mapper;

    public List<ModelActionRead> read() {
        return store.getModelActions()
                .stream()
                .map(mapper::toRead)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
