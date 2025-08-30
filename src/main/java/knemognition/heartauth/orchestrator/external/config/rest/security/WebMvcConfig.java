package knemognition.heartauth.orchestrator.external.config.rest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final PairingJwtInterceptor interceptor;
    @Override public void addInterceptors(InterceptorRegistry r) { r.addInterceptor(interceptor); }
}