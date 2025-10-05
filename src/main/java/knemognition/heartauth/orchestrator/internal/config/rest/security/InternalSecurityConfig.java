package knemognition.heartauth.orchestrator.internal.config.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;

import static knemognition.heartauth.orchestrator.shared.config.mdc.HeaderNames.ATTR_TENANT_ID;

@Configuration
class InternalSecurityConfig {

    private static void writeProblem(HttpServletResponse res,
                                     org.springframework.http.ProblemDetail pd,
                                     HttpStatus status,
                                     ObjectMapper om) throws java.io.IOException {
        res.setStatus(status.value());
        res.setContentType("application/problem+json");
        om.writeValue(res.getOutputStream(), pd);
    }

    @Bean()
    SecurityFilterChain internal(HttpSecurity http,
                                 InternalApiKeyAuthenticationProvider provider,
                                 ObjectMapper objectMapper) throws Exception {

        var manager = new ProviderManager(provider);
        var converter = new InternalApiKeyAuthenticationConverter();
        var filter = new AuthenticationFilter(manager, converter);

        filter.setSuccessHandler((req, res, auth) -> {
            if (auth instanceof InternalApiKeyAuthenticationToken tok && tok.isAuthenticated()) {
                req.setAttribute(ATTR_TENANT_ID, tok.getPrincipal());
            }
        });

        AuthenticationEntryPoint problemEntryPoint = (req, res, ex) -> {
            var pd = knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or missing API key.",
                    req,
                    ex != null ? new RuntimeException("invalid_api_key") : new RuntimeException("unauthorized")
            );
            writeProblem(res, pd, HttpStatus.UNAUTHORIZED, objectMapper);
        };
        filter.setFailureHandler(new AuthenticationEntryPointFailureHandler(problemEntryPoint));

        return http
                .securityMatcher("/internal/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/internal/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(problemEntryPoint))
                .addFilter(filter)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
