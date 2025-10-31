package knemognition.heartauth.orchestrator.security.app.handlers;

import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatePublicKeyPemHandler {
    private final PemMapper pemMapper;

    public void handle(String publicKeyPem) {
        pemMapper.publicMapAndValidate(publicKeyPem);
    }
}
