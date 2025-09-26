package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NonceValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import test.config.HeartauthUnitTest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static knemognition.heartauth.orchestrator.shared.utils.KeyLoader.loadEcPublicKey;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateNonceServiceImplTest extends HeartauthUnitTest {

    final String PUBLIC_KEY_PEM =
            "-----BEGIN PUBLIC KEY-----" +
                    "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE8/2k2MUkTlixFvR9ooUgBOwKgj5v" +
                    "BMVf3NGFOgV3TJcwaZme8fTcNjPxSmSrMoPd3LS6OJ+IMzNkQO5CVI79Vw==" +
                    "-----END PUBLIC KEY-----";
    final String NONCE =
            "2iS7zMVqgV1dicY3toudE3dvGzi8jsu7fguS3OmYe6Q=";
    final String VALID_SIGNATURE =
            "TF9VYe1eTAHoRq1sj3NbfygmZYSZ_jPHHck7-yDN96PMChMQ8wDzxL2Tbqn0aEpVhLiZoujlkXH6Btn431YRtA";
    final String INVALID_SIGNATURE =
            "TF9VYe1eTAHoRq1sjsNbfygmZYSZ_jPHHck7-yDN96PMChMQ8wDzxL2Tbqn0aEpVhLiZoujlkXH6Btn431YRtA";
    @InjectMocks
    ValidateNonceServiceImpl service;

    @Test
    void validate_withCorrectSignature_succeeds() throws Exception {
        ValidateNonce req = ValidateNonce.builder().nonce(NONCE)
                .signature(VALID_SIGNATURE)
                .pub(loadEcPublicKey(new ByteArrayInputStream(PUBLIC_KEY_PEM.getBytes(StandardCharsets.UTF_8))))
                .build();

        assertDoesNotThrow(() -> service.validate(req));
    }

    @Test
    void validate_withIncorrectSignature_fails() throws Exception {
        ValidateNonce req = ValidateNonce.builder().nonce(NONCE)
                .signature(INVALID_SIGNATURE)
                .pub(loadEcPublicKey(new ByteArrayInputStream(PUBLIC_KEY_PEM.getBytes(StandardCharsets.UTF_8))))
                .build();

        assertThrows(NonceValidationException.class, () -> service.validate(req));
    }

}