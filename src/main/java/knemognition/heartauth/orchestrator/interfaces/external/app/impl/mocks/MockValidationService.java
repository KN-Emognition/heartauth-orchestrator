package knemognition.heartauth.orchestrator.interfaces.external.app.impl.mocks;

import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.interfaces.external.app.domain.DecryptJwe;
import knemognition.heartauth.orchestrator.interfaces.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Primary
@Profile(SpringProfiles.DEV)
public class MockValidationService {

    public void validateNonce(ValidateNonce request) {
        log.warn("⚠️ Skipping nonce validation (AlwaysPassValidateNonceService is active).");
    }

    public JWTClaimsSet decryptAndVerifyJwe(DecryptJwe jwt) {
        log.warn("⚠️ Skipping JWE decryption and verification (MockValidationService is active).");
        return new JWTClaimsSet.Builder()
                .claim("refEcg", createRandomReference())  // List<List<Float>>
                .claim("testEcg", createRandomTestVector()) // List<Float>
                .build();
    }

    public void validatePublicKeyPem(String pem) {
        log.warn("⚠️ Skipping public key PEM validation (MockValidationService is active).");
    }

    private List<Float> createRandomTestVector() {
        Random random = new Random();
        List<Float> vector = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            float value = random.nextFloat() * 100;
            vector.add(value);
        }
        return vector;
    }

    private List<List<Float>> createRandomReference() {
        List<List<Float>> reference = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            reference.add(createRandomTestVector());
        }
        return reference;
    }


}
