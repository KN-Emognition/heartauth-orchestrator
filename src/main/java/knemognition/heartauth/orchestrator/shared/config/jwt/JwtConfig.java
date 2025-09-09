package knemognition.heartauth.orchestrator.shared.config.jwt;

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
import knemognition.heartauth.orchestrator.internal.config.pairing.InternalPairingProperties;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean("pairingPrivateKey")
    ECPrivateKey pairingPrivateKey(JwtProperties p) throws Exception {
        try (var in = p.privateKeyLocation().getInputStream()) {
            return KeyLoader.loadEcPrivateKey(in);
        }
    }

    @Bean("pairingPublicJwk")
    ECKey pairingPublicJwk(@Qualifier("pairingPublicKey") ECPublicKey pub, JwtProperties p) {
        return new ECKey.Builder(Curve.P_256, pub)
                .keyID(p.kid())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .build();
    }

    @Bean("pairingPublicKey")
    ECPublicKey pairingPublicKey(JwtProperties p) throws Exception {
        try (var in = p.publicKeyLocation().getInputStream()) {
            return KeyLoader.loadEcPublicKey(in);
        }
    }

    @Bean("pairingJwtEncoder")
    JwtEncoder pairingJwtEncoder(
            @Qualifier("pairingPrivateKey") ECPrivateKey priv,
            @Qualifier("pairingPublicKey") ECPublicKey pub,
            JwtProperties p) {

        ECKey jwk = new ECKey.Builder(Curve.P_256, pub)
                .privateKey(priv)
                .keyID(p.kid())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }



}
