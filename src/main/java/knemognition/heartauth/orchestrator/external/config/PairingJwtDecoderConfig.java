package knemognition.heartauth.orchestrator.external.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@RequiredArgsConstructor
public class PairingJwtDecoderConfig {

    @Bean
    public JwtDecoder pairingJwtDecoderDev() {
        // 32+ bytes dummy secret to satisfy HS256
        var key = new SecretKeySpec("a-string-secret-at-least-256-bits-long".getBytes(), "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();
        // Disable all validations (even exp). Keep if you want exp checks: decoder.setJwtValidator(JwtValidators.createDefault());
        decoder.setJwtValidator(jwt -> OAuth2TokenValidatorResult.success());
        return decoder;
    }
}
