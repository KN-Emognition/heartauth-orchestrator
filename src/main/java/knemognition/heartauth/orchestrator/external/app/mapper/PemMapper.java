package knemognition.heartauth.orchestrator.external.app.mapper;

import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.PemParsingException;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPublicKey;

@Mapper(componentModel = "spring")
public interface PemMapper {

    @Named("pemToEcPublicKey")
    default ECPublicKey map(String pem) {
        try (var in = new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8))) {
            ECPublicKey ecPub = KeyLoader.loadEcPublicKey(in);

            if (!"EC".equalsIgnoreCase(ecPub.getAlgorithm())) {
                throw new PemParsingException("Public key must be EC");
            }

            var params = ecPub.getParams();
            if (params == null || params.getCurve().getField().getFieldSize() != 256) {
                throw new PemParsingException("EC key must be P-256 (secp256r1)");
            }

            return ecPub;
        } catch (Exception e) {
            throw new PemParsingException("Invalid EC public key (expected PEM X.509 P-256)");
        }
    }
}
