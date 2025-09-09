package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.ports.in.ValidateNonceService;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NonceValidationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.Base64;

@Service
public class ValidateNonceServiceImpl implements ValidateNonceService {

    @Override
    public void validate(ValidateNonce request) {
        try {
            Signature verifier = Signature.getInstance("SHA256withECDSAinP1363Format");
            verifier.initVerify(request.getPub());
            verifier.update(request.getNonce().getBytes(StandardCharsets.UTF_8));

            byte[] sig = Base64.getUrlDecoder().decode(request.getSignature());
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