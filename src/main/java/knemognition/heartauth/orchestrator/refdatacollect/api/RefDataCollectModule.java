package knemognition.heartauth.orchestrator.refdatacollect.api;

import org.springframework.modulith.NamedInterface;

import java.util.Optional;

@NamedInterface
public interface RefDataCollectModule {

    Optional<CreateRefDataCollectProcessRead> createProcess(CreateRefDataCollectProcessCmd cmd);

    Optional<CreateRefDataCollectNotificationRead> createNotification(CreateRefDataCollectNotificationCmd cmd);
}
