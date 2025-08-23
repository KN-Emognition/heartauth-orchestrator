package knemognition.heartauth.orchestrator.internal.config.errorhandling.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.FcmSendException;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RestControllerAdvice(basePackages = "knemognition.heartauth.orchestrator.internal.interfaces.rest")
public class ApiExceptionHandler {
    @ExceptionHandler(NoActiveDeviceException.class)
    public ProblemDetail handleNoActiveDevice(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No active device for given user.", req, ex);
    }
    @ExceptionHandler(FcmSendException.class)
    public ProblemDetail handleFcmSend(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_GATEWAY, "Failed to send notification via FCM.", req, ex);
    }

    private ProblemDetail problem(HttpStatus status, String title, HttpServletRequest req, Exception ex) {
        var pd = ProblemDetail.forStatusAndDetail(status, title);
        pd.setTitle(title);
        pd.setInstance(URI.create(req.getRequestURI()));
        enrich(pd, req, ex);
        if (status.is5xxServerError()) {
            log.error("{} {} -> {}", req.getMethod(), req.getRequestURI(), ex.toString(), ex);
        } else {
            log.warn("{} {} -> {} {}", req.getMethod(), req.getRequestURI(), status.value(), title);
        }
        return pd;
    }


    private void enrich(ProblemDetail pd, HttpServletRequest req, Exception ex) {
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", req.getRequestURI());
        var rid = req.getHeader("X-Request-Id");
        if (rid != null && !rid.isBlank()) pd.setProperty("requestId", rid);
    }

    private static String pathOf(ConstraintViolation<?> v) {
        var path = v.getPropertyPath();
        return path == null ? "" : path.toString();
    }

}