package knemognition.heartauth.orchestrator.security.app;

import com.nimbusds.jose.JOSEException;
import knemognition.heartauth.orchestrator.security.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.SecurityModule;
import knemognition.heartauth.orchestrator.security.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.handlers.CreateNonceHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.DecryptJweHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.ValidateNonceHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.ValidatePublicKeyPemHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.createEphemeralKeyPair.CreateEphemeralKeyPairHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class SecurityModuleImpl implements SecurityModule {

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
