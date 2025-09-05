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
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;


@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean("pairingPrivateKey")
    public ECPrivateKey pairingPrivateKey(JwtProperties p) throws Exception {
        Resource r = p.privateKeyLocation();
        try (var in = r.getInputStream()) {
            char[] pwd = p.privateKeyPassword() != null ? p.privateKeyPassword().toCharArray() : null;
            return Crypto.loadEcPrivateKey(in, pwd);
        }
    }

    @Bean("pairingPublicKey")
    public ECPublicKey pairingPublicKey(JwtProperties p) throws Exception {
        Resource r = p.publicKeyLocation();
        try (var in = r.getInputStream()) {
            return Crypto.loadEcPublicKey(in);
        }
    }

    @Bean
    @Qualifier("pairingSigningJwk")
    public com.nimbusds.jose.jwk.ECKey signingJwk(JwtProperties p,
                                                  @Qualifier("pairingPublicKey") ECPublicKey pub,
                                                  @Qualifier("pairingPrivateKey") ECPrivateKey priv) {
        return new com.nimbusds.jose.jwk.ECKey.Builder(Curve.P_256, pub)
                .privateKey(priv)
                .keyUse(KeyUse.SIGNATURE)
                .keyID(p.kid())
                .algorithm(JWSAlgorithm.ES256).build();
    }

    @Bean("pairingJwtDecoder")
    NimbusJwtDecoder pairingJwtDecoder(@Qualifier("pairingPublicKey") ECPublicKey publicKey) {
        ECKey ecKey = new ECKey.Builder(Curve.P_256, publicKey)
                .keyUse(KeyUse.SIGNATURE)
                .build();

        JWKSet jwkSet = new JWKSet(ecKey);
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(new JWSVerificationKeySelector<>(
                JWSAlgorithm.ES256,
                jwkSource
        ));

        return new NimbusJwtDecoder(jwtProcessor);
    }

}
