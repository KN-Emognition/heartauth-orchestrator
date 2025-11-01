package knemognition.heartauth.orchestrator.security.app.handlers.createEphemeralKeyPair;

import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.security.KeyPair;


@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@Profile(SpringProfiles.E2E)
public class CreateEphemeralKeyPairHandlerMock implements CreateEphemeralKeyPairHandler {

    private final KeyPair pairingEcKeyPair;

    public KeyPair handle() {
        log.warn("Mock ephemeral key pair creation called");
        return pairingEcKeyPair;
    }
}
