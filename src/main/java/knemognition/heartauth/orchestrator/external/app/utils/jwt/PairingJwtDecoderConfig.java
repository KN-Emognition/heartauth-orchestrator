package knemognition.heartauth.orchestrator.external.app.utils.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@RequiredArgsConstructor
public class PairingJwtDecoderConfig {

    private final PairingJwtProps props;

    @Bean
    public JwtDecoder pairingJwtDecoder() {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(props.getIssuer());

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(props.getIssuer());
        OAuth2TokenValidator<Jwt> withAudience = jwt -> {
            var aud = jwt.getAudience();
            return (aud != null && aud.contains(props.getAudience()))
                    ? OAuth2TokenValidatorResult.success()
                    : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token",
                    "Required audience not present: " + props.getAudience(), null));
        };

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        return decoder;
    }
}
