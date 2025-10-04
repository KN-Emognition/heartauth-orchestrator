package knemognition.heartauth.orchestrator.external.app.impl.mocks;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.DecryptJwe;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Service
@Primary
@Profile("dev")
public class MockValidationService implements ExternalValidationService {

    @Override
    public void validateNonce(ValidateNonce request) {
        log.warn("⚠️ Skipping nonce validation (AlwaysPassValidateNonceService is active).");
    }
    @Override
    public JWTClaimsSet decryptAndVerifyJwe(DecryptJwe jwt) {
        log.warn("⚠️ Skipping JWE decryption and verification (MockValidationService is active).");
        return null;
    }
    @Override
    public void validatePublicKeyPem(String pem) {
        log.warn("⚠️ Skipping public key PEM validation (MockValidationService is active).");
    }
}
