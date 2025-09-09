package knemognition.heartauth.orchestrator.external.app.service.mocks;

import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.ports.in.ValidateNonceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")
public class MockValidateService implements ValidateNonceService {

    @Override
    public void validate(ValidateNonce request) {
        log.warn("⚠️ Skipping nonce validation (AlwaysPassValidateNonceService is active).");
    }
}
