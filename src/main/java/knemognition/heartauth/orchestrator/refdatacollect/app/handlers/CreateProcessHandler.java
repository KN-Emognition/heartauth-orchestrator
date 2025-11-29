package knemognition.heartauth.orchestrator.refdatacollect.app.handlers;


import knemognition.heartauth.orchestrator.refdatacollect.api.CreateRefDataCollectProcessCmd;
import knemognition.heartauth.orchestrator.refdatacollect.api.CreateRefDataCollectProcessRead;
import knemognition.heartauth.orchestrator.refdatacollect.app.mappers.RefDataCollectMapper;
import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity.RefDataCollectProcessEntity;
import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.repository.RefDataCollectProcessRepository;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateProcessHandler {

    private final UserModule userModule;
    private final RefDataCollectProcessRepository store;
    private final RefDataCollectMapper mapper;

    public Optional<CreateRefDataCollectProcessRead> handle(CreateRefDataCollectProcessCmd cmd) {
        return userModule.getUser(mapper.toCmd(cmd))
                .map(userRead -> {
                    log.info(
                            "[ECG-REF-DATA-COLLECT] Creating reference data collection process for userId: {}",
                            userRead.getUserId()
                    );
                    var created = store.save(createProcess(userRead.getId()));
                    return CreateRefDataCollectProcessRead.builder()
                            .id(created.getProcessId())
                            .build();
                });
    }

    private RefDataCollectProcessEntity createProcess(UUID userId) {
        var entity = new RefDataCollectProcessEntity();
        entity.setProcessId(UUID.randomUUID());
        entity.setUserId(userId);
        entity.setSamplesToBeCollected(5);
        entity.setInterval(Duration.ofMinutes(5)); // make it configurable later
        entity.setSendNotification(true);
        entity.setLastNotificationAt(OffsetDateTime.now());
        return entity;
    }
}
