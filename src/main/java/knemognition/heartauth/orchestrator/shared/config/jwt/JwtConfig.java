package knemognition.heartauth.orchestrator.shared.config.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Objects;
import java.util.UUID;

@Configuration
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {


    private boolean isUsable(Resource r) {
        return r != null && r.exists();
    }

    private KeyPair generateEcP256() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec("secp256r1"));
        return kpg.generateKeyPair();
    }

    private String resolveKid(JwtProperties p, ECPublicKey pub) {
        if (p.kid() != null && !p.kid()
                .isBlank()) return p.kid();
        try {
            ECKey tmp = new ECKey.Builder(Curve.P_256, pub).build();
            return tmp.computeThumbprint()
                    .toString();
        } catch (Exception e) {
            return UUID.randomUUID()
                    .toString();
        }
    }


    @Bean("pairingEcKeyPair")
    KeyPair pairingEcKeyPair(JwtProperties p) throws Exception {
        boolean haveFiles = isUsable(p.privateKeyLocation()) && isUsable(p.publicKeyLocation());
        if (haveFiles) {
            try (var inPriv = p.privateKeyLocation()
                    .getInputStream();
                 var inPub = p.publicKeyLocation()
                         .getInputStream()) {
                ECPrivateKey priv = KeyLoader.loadEcPrivateKey(inPriv);
                ECPublicKey pub = KeyLoader.loadEcPublicKey(inPub);
                return new KeyPair(pub, priv);
            } catch (IOException ioe) {
                log.warn("RUNNING WITH GENERATED KEYS - NOT FOR PRODUCTION USE");
                return generateEcP256();
            }
        }
        log.warn("RUNNING WITH GENERATED KEYS - NOT FOR PRODUCTION USE");
        return generateEcP256();
    }


    @Bean("pairingPrivateKey")
    ECPrivateKey pairingPrivateKey(@Qualifier("pairingEcKeyPair") KeyPair kp) {
        return (ECPrivateKey) Objects.requireNonNull(kp.getPrivate(), "EC private key missing");
    }

    @Bean("pairingPublicKey")
    ECPublicKey pairingPublicKey(@Qualifier("pairingEcKeyPair") KeyPair kp) {
        return (ECPublicKey) Objects.requireNonNull(kp.getPublic(), "EC public key missing");
    }

    @Bean("pairingPublicJwk")
    ECKey pairingPublicJwk(@Qualifier("pairingPublicKey") ECPublicKey pub, JwtProperties p) {
        return new ECKey.Builder(Curve.P_256, pub)
                .keyID(resolveKid(p, pub))
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .build();
    }

    @Bean("pairingJwtEncoder")
    JwtEncoder pairingJwtEncoder(
            @Qualifier("pairingPrivateKey") ECPrivateKey priv,
            @Qualifier("pairingPublicKey") ECPublicKey pub,
            JwtProperties p) {

        String kid = resolveKid(p, pub);

        ECKey jwk = new ECKey.Builder(Curve.P_256, pub)
                .privateKey(priv)
                .keyID(kid)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
