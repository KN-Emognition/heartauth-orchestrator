package zpi.heartAuth.orchestrator.config.errorHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.BAD_REQUEST, "Validation failed", req, ex);
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        fe -> fe.getField(),
                        LinkedHashMap::new,
                        Collectors.mapping(fe -> (fe.getDefaultMessage() == null ? "Invalid value" : fe.getDefaultMessage()),
                                Collectors.toList())));
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.BAD_REQUEST, "Constraint violation", req, ex);
        Map<String, List<String>> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        v -> pathOf(v),
                        LinkedHashMap::new,
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Malformed JSON request", req, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String detail = "Parameter '%s' has invalid value '%s'".formatted(ex.getName(), ex.getValue());
        var pd = problem(HttpStatus.BAD_REQUEST, "Type mismatch", req, ex);
        pd.setDetail(detail);
        return pd;
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ProblemDetail handleNoHandler(NoHandlerFoundException ex, HttpServletRequest req) {
        return problem(HttpStatus.NOT_FOUND, "No handler for %s %s".formatted(ex.getHttpMethod(), ex.getRequestURL()), req, ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", req, ex);
        pd.setProperty("allowed", ex.getSupportedHttpMethods());
        return pd;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type", req, ex);
        pd.setProperty("supported", ex.getSupportedMediaTypes());
        return pd;
    }


    @ExceptionHandler({ResponseStatusException.class, ErrorResponseException.class})
    public ProblemDetail handleStatusExceptions(Exception ex, HttpServletRequest req) {
        if (ex instanceof ErrorResponseException ere) {
            var pd = ere.getBody();
            enrich(pd, req, ex);
            return pd;
        }
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return problem(HttpStatus.FORBIDDEN, "Access is denied", req, ex);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAny(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, ex);
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
