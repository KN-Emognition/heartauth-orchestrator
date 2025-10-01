package knemognition.heartauth.orchestrator.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;

import static knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem;

@Configuration
class AdminSecurityConfig {

    private static void writeProblem(HttpServletResponse res,
                                     org.springframework.http.ProblemDetail pd,
                                     HttpStatus status,
                                     ObjectMapper om) throws java.io.IOException {
        res.setStatus(status.value());
        res.setContentType("application/problem+json");
        om.writeValue(res.getOutputStream(), pd);
    }

    @Bean
    SecurityFilterChain admin(HttpSecurity http,
                              AdminApiKeyAuthenticationProvider provider,
                              ObjectMapper objectMapper) throws Exception {

        var manager = new ProviderManager(provider);
        var converter = new AdminApiKeyAuthenticationConverter();
        var filter = new AuthenticationFilter(manager, converter);

        filter.setSuccessHandler((req, res, auth) -> {
        });

        AuthenticationEntryPoint problemEntryPoint = (req, res, ex) -> {
            var pd = problem(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or missing API key.",
                    req,
                    ex != null ? new RuntimeException("invalid_api_key") : new RuntimeException("unauthorized")
            );
            writeProblem(res, pd, HttpStatus.UNAUTHORIZED, objectMapper);
        };
        filter.setFailureHandler(new AuthenticationEntryPointFailureHandler(problemEntryPoint));

        return http
                .securityMatcher("/admin/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.OPTIONS, "/admin/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(problemEntryPoint))
                .addFilterBefore(filter, AuthorizationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
