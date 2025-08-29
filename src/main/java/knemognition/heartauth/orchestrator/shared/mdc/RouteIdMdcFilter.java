package knemognition.heartauth.orchestrator.shared.mdc;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RouteIdMdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String routeId = request.getHeader("X-Route-Id");
        if (routeId == null || routeId.isBlank()) {
            routeId = request.getRequestURI();
            if (routeId == null || routeId.isBlank()) {
                routeId = UUID.randomUUID().toString();
            }
        }
        MDC.put("routeId", routeId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("routeId");
        }
    }
}
