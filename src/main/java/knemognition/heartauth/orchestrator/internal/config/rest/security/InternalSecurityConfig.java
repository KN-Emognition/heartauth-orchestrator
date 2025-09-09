package knemognition.heartauth.orchestrator.internal.config.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class InternalSecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain internal(HttpSecurity http,
                                 ApiKeyAuthenticationProvider provider,
                                 ObjectMapper objectMapper) throws Exception {

        var manager   = new ProviderManager(provider);
        var converter = new ApiKeyAuthenticationConverter();
        var filter    = new AuthenticationFilter(manager, converter);

        filter.setSuccessHandler((req, res, auth) -> {});
        AuthenticationEntryPoint problemEntryPoint = (req, res, ex) -> {
            var pd = ExceptionHandlingUtils.problem(
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

    private static void writeProblem(HttpServletResponse res,
                                     org.springframework.http.ProblemDetail pd,
                                     HttpStatus status,
                                     ObjectMapper om) throws java.io.IOException {
        res.setStatus(status.value());
        res.setContentType("application/problem+json");
        om.writeValue(res.getOutputStream(), pd);
    }
}
