package knemognition.heartauth.orchestrator.modelaction.api;

import java.util.List;

public interface ModelActionApi {
    void save(SaveModelActionCmd cmd);

    List<ModelActionRead> read();
}
