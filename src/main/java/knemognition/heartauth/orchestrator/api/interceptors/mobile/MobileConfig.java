package knemognition.heartauth.orchestrator.api.interceptors.mobile;

import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class MobileConfig {

    @Bean
    @Profile(SpringProfiles.MOBILE)
    SecurityFilterChain pairing(HttpSecurity http, JwtDecoder decoder) throws Exception {
        return http.securityMatcher("/mobile/v1/pairing/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a.anyRequest()
                        .authenticated())
                .oauth2ResourceServer(o -> o.jwt(j -> j.decoder(decoder)))
                .exceptionHandling(e -> e.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .build();
    }
}
