package knemognition.heartauth.orchestrator.security;

import com.nimbusds.jose.jwk.ECKey;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import test.config.HeartauthSpringBootTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(SpringProfiles.ADMIN)
class SecurityJwtIntegrationTest extends HeartauthSpringBootTest {

    @Autowired
    private JwtEncoder pairingJwtEncoder;
    @Autowired
    private ECKey pairingPublicJwk;
    @Autowired
    @Qualifier("pairingJwtDecoder")
    private JwtDecoder pairingJwtDecoder;
    @Autowired
    private PairingProperties pairingProperties;

    @Test
    void shouldIssueAndVerifyEs256Token() {
        Instant now = Instant.now();
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.ES256).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(pairingProperties.getIssuer())
                .subject("tenant-under-test")
                .audience(pairingProperties.getAudience())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(120))
                .build();

        Jwt signed = pairingJwtEncoder.encode(JwtEncoderParameters.from(header, claims));
        assertThat(signed.getTokenValue()).isNotBlank();
        assertThat(signed.getHeaders().get("kid")).isEqualTo(pairingPublicJwk.getKeyID());

        Jwt decoded = pairingJwtDecoder.decode(signed.getTokenValue());
        assertThat(decoded.getSubject()).isEqualTo("tenant-under-test");
        assertThat(decoded.getAudience()).containsAll(pairingProperties.getAudience());
    }
}
