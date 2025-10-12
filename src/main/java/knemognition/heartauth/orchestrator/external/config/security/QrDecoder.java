package knemognition.heartauth.orchestrator.external.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import knemognition.heartauth.orchestrator.external.config.pairing.ExternalPairingProperties;
import knemognition.heartauth.orchestrator.shared.config.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;

import java.security.interfaces.ECPublicKey;
import java.time.Duration;
import java.util.Collections;

@Configuration
public class QrDecoder {
    @Bean("pairingJwtDecoder")
    JwtDecoder pairingJwtDecoder(@Qualifier("pairingPublicKey") ECPublicKey pub, JwtProperties p, ExternalPairingProperties externalPairingProperties) {
        ECKey publicJwk = new ECKey.Builder(Curve.P_256, pub)
                .keyID(p.kid())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(publicJwk));
        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.ES256, jwkSource));
        NimbusJwtDecoder dec = new NimbusJwtDecoder(jwtProcessor);
        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer(externalPairingProperties.getIssuer());

        OAuth2TokenValidator<Jwt> withAudience = jwt ->
                (jwt.getAudience() != null && !Collections.disjoint(jwt.getAudience(),
                        externalPairingProperties.getAudience()))
                        ? OAuth2TokenValidatorResult.success()
                        : OAuth2TokenValidatorResult.failure(
                        new OAuth2Error("invalid_token", "missing/invalid audience", ""));

        JwtTimestampValidator skew = new JwtTimestampValidator(Duration.ofSeconds(30));

        dec.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience, skew));
        return dec;
    }
}
