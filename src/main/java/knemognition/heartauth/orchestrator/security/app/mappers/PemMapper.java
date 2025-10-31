package knemognition.heartauth.orchestrator.security.app.mappers;

import knemognition.heartauth.orchestrator.security.PemParsingException;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Mapper(componentModel = "spring")
public interface PemMapper {

    @Named("pemToEcPublicKey")
    default ECPublicKey publicMapAndValidate(String pem) {
        try (var in = new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8))) {
            ECPublicKey ecPub = KeyLoader.loadEcPublicKey(in);

            if (!"EC".equalsIgnoreCase(ecPub.getAlgorithm())) {
                throw new PemParsingException("Public key must be EC");
            }

            var params = ecPub.getParams();
            if (params == null || params.getCurve()
                    .getField()
                    .getFieldSize() != 256) {
                throw new PemParsingException("EC key must be P-256 (secp256r1)");
            }

            return ecPub;
        } catch (Exception e) {
            throw new PemParsingException("Invalid EC public key (expected PEM X.509 P-256)");
        }
    }

    @Named("pemToEcPrivateKey")
    default ECPrivateKey privateMapAndValidate(String pem) {
        try (var in = new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8))) {
            ECPrivateKey ecPriv = KeyLoader.loadEcPrivateKey(in);

            if (!"EC".equalsIgnoreCase(ecPriv.getAlgorithm())) {
                throw new PemParsingException("Private key must be EC");
            }

            var params = ecPriv.getParams();
            if (params == null || params.getCurve()
                    .getField()
                    .getFieldSize() != 256) {
                throw new PemParsingException("EC key must be P-256 (secp256r1)");
            }

            return ecPriv;
        } catch (Exception e) {
            throw new PemParsingException("Invalid EC private key (expected PEM PKCS#8 P-256)");
        }
    }
}
