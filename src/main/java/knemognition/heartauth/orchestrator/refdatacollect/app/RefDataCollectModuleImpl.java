package knemognition.heartauth.orchestrator.refdatacollect.app;

import knemognition.heartauth.orchestrator.refdatacollect.api.*;
import knemognition.heartauth.orchestrator.refdatacollect.app.handlers.CreateNotificationHandler;
import knemognition.heartauth.orchestrator.refdatacollect.app.handlers.CreateProcessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class RefDataCollectModuleImpl implements RefDataCollectModule {

    private final CreateProcessHandler createProcessHandler;
    private final CreateNotificationHandler createNotificationHandler;

    @Override
    public Optional<CreateRefDataCollectProcessRead> createProcess(CreateRefDataCollectProcessCmd cmd) {
        return createProcessHandler.handle(cmd);
    }

    @Override
    public Optional<CreateRefDataCollectNotificationRead> createNotification(CreateRefDataCollectNotificationCmd cmd) {
        return createNotificationHandler.handle(cmd);
    }
}
