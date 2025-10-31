package knemognition.heartauth.orchestrator.security.app.handlers;

import knemognition.heartauth.orchestrator.security.api.NonceValidationException;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ValidateNonceHandler {
    private final PemMapper pemMapper;

    public void handle(ValidateNonceCmd cmd) {
        try {
            var verifier = Signature.getInstance("SHA256withECDSAinP1363Format");
            var publicKey = pemMapper.publicMapAndValidate(cmd.getPub());
            verifier.initVerify(publicKey);
            verifier.update(cmd.getNonce()
                    .getBytes(StandardCharsets.UTF_8));

            byte[] sig = Base64.getUrlDecoder()
                    .decode(cmd.getSignature());
            if (sig.length != 64) {
                throw new NonceValidationException("ES256 signature must be 64 bytes (R||S).");
            }
            if (!verifier.verify(sig)) {
                throw new NonceValidationException("Invalid signature.");
            }
        } catch (Exception e) {
            throw new NonceValidationException("Failed to verify ES256 signature");
        }
    }
}
