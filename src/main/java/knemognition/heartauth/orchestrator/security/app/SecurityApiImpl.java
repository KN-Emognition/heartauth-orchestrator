package knemognition.heartauth.orchestrator.security.app;

import com.nimbusds.jose.JOSEException;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityApi;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.handlers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class SecurityApiImpl implements SecurityApi {

    private final ValidateNonceHandler validateNonceHandler;
    private final ValidatePublicKeyPemHandler validatePublicKeyPemHandler;
    private final DecryptJweHandler decryptJweHandler;
    private final CreateNonceHandler createNonceHandler;
    private final CreateEphemeralKeyPairHandler createEphemeralKeyPairHandler;

    @Override
    public void validateNonce(ValidateNonceCmd cmd) {
        validateNonceHandler.handle(cmd);
    }

    @Override
    public <T> T decryptJwe(DecryptJweCmd<T> cmd) throws ParseException, JOSEException {
        return decryptJweHandler.handle(cmd);
    }

    @Override
    public void validatePublicKeyPem(String pem) {
        validatePublicKeyPemHandler.handle(pem);
    }

    @Override
    public String createNonce(int length) {
        return createNonceHandler.handle(length);
    }

    @Override
    public KeyPair createEphemeralKeyPair() {
        return createEphemeralKeyPairHandler.handle();
    }
}
