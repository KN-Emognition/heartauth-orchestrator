package knemognition.heartauth.orchestrator.modelaction.app;

import knemognition.heartauth.orchestrator.modelaction.api.ModelActionRead;
import knemognition.heartauth.orchestrator.modelaction.api.ModelActionApi;
import knemognition.heartauth.orchestrator.modelaction.api.SaveModelActionCmd;
import knemognition.heartauth.orchestrator.modelaction.app.commands.ModelActionReadHandler;
import knemognition.heartauth.orchestrator.modelaction.app.commands.ModelActionSaveHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModelActionServiceImpl implements ModelActionApi {


    private final ModelActionReadHandler modelActionReadHandler;
    private final ModelActionSaveHandler modelActionSaveHandler;

    @Override
    public void save(SaveModelActionCmd cmd) {
        modelActionSaveHandler.save(cmd);

    }

    @Override
    public List<ModelActionRead> read() {
        return modelActionReadHandler.read();
    }
}
