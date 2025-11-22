package knemognition.heartauth.orchestrator.security.app;

import com.nimbusds.jose.JOSEException;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.handlers.CreateNonceHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.DecryptJweHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.ValidateNonceHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.ValidatePublicKeyPemHandler;
import knemognition.heartauth.orchestrator.security.app.handlers.createEphemeralKeyPair.CreateEphemeralKeyPairHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityModuleImplTest {

    @Mock
    private ValidateNonceHandler validateNonceHandler;
    @Mock
    private ValidatePublicKeyPemHandler validatePublicKeyPemHandler;
    @Mock
    private DecryptJweHandler decryptJweHandler;
    @Mock
    private CreateNonceHandler createNonceHandler;
    @Mock
    private CreateEphemeralKeyPairHandler createEphemeralKeyPairHandler;

    @InjectMocks
    private SecurityModuleImpl module;

    @Test
    void shouldDelegateNonceValidation() {
        var cmd = ValidateNonceCmd.builder()
                .nonce("nonce")
                .signature("sig")
                .pub("pub")
                .build();

        module.validateNonce(cmd);

        verify(validateNonceHandler).handle(cmd);
    }

    @Test
    void shouldDelegateJweDecryption() throws ParseException, JOSEException {
        DecryptJweCmd<String> cmd = DecryptJweCmd.<String>builder()
                .jwe("token")
                .senderPublicKey("pub")
                .recipientPrivateKey("priv")
                .build();
        when(decryptJweHandler.handle(cmd)).thenReturn("ok");

        String result = module.decryptJwe(cmd);

        assertThat(result).isEqualTo("ok");
        verify(decryptJweHandler).handle(cmd);
    }

    @Test
    void shouldDelegatePublicKeyValidation() {
        module.validatePublicKeyPem("pem");
        verify(validatePublicKeyPemHandler).handle("pem");
    }

    @Test
    void shouldDelegateNonceCreation() {
        when(createNonceHandler.handle(32)).thenReturn("nonce");

        assertThat(module.createNonce(32)).isEqualTo("nonce");
    }

    @Test
    void shouldDelegateEphemeralKeyPairCreation() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair pair = generator.generateKeyPair();
        when(createEphemeralKeyPairHandler.handle()).thenReturn(pair);

        assertThat(module.createEphemeralKeyPair()).isEqualTo(pair);
    }
}
