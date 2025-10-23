package knemognition.heartauth.orchestrator.internal.app.impl.mocks;

import knemognition.heartauth.orchestrator.internal.app.ports.in.KeyCreatorService;
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
public class MockKeyCreator implements KeyCreatorService {

    private final KeyPair pairingEcKeyPair;

    @Override
    public KeyPair createEphemeralKeyPair() {
        log.warn("Mock ephemeral key pair creation called");
        return pairingEcKeyPair;
    }
}
