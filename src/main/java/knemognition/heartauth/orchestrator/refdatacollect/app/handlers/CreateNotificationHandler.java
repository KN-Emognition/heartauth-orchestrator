package knemognition.heartauth.orchestrator.refdatacollect.app.handlers;


import knemognition.heartauth.orchestrator.refdatacollect.api.CreateRefDataCollectNotificationCmd;
import knemognition.heartauth.orchestrator.refdatacollect.api.CreateRefDataCollectNotificationRead;
import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.repository.RefDataCollectProcessRepository;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateNotificationHandler {

    private final UserModule userModule;
    private final SecurityModule securityModule;
    private final RefDataCollectProcessRepository refDataCollectProcessRepository;

    public Optional<CreateRefDataCollectNotificationRead> handle(CreateRefDataCollectNotificationCmd cmd) {
        var process = refDataCollectProcessRepository.findByProcessId(cmd.getProcessId())
                .orElseThrow();
        var user = userModule.getUser(process.getUserId())
                .orElseThrow();
        var keyPair = securityModule.createEphemeralKeyPair();

        return Optional.empty();
    }

}
