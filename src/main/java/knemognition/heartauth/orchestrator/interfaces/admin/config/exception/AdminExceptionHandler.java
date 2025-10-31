package knemognition.heartauth.orchestrator.interfaces.admin.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.TenantControllerImpl;
import knemognition.heartauth.orchestrator.tenant.api.TenantApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem;

@Slf4j
@RestControllerAdvice(basePackageClasses = {
        TenantControllerImpl.class
})
public class AdminExceptionHandler {
    @ExceptionHandler(TenantApiException.class)
    public ProblemDetail handleNoActiveDevice(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Tenant Api Exception.", req, ex);
    }
}