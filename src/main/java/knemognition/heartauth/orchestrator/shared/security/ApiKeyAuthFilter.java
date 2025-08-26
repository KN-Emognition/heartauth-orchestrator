package knemognition.heartauth.orchestrator.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "X-API-Key";
    private final ApiKeyProperties props;

    public ApiKeyAuthFilter(ApiKeyProperties props) {
        this.props = props;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true; // CORS preflight
        String p = req.getRequestURI();
        return "/".equals(p) || p.startsWith("/actuator/health");     // public paths
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String provided = req.getHeader(AUTH_HEADER);
        var match = props.keys().stream().filter(k -> k.matches(provided)).findFirst().orElse(null);
        if (match == null) {
            chain.doFilter(req, res); // let it through; @PreAuthorize will block if required
            return;
        }
        System.out.print(match.roles().stream().map(SimpleGrantedAuthority::new).toList().toString());
        var auth = new UsernamePasswordAuthenticationToken(
                match.id(), null,
                match.roles().stream().map(SimpleGrantedAuthority::new).toList()); // attach roles here
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(req, res);
    }
}
