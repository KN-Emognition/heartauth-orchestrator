package knemognition.heartauth.orchestrator.admin.config.errorhandling.handler;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.admin.config.errorhandling.exception.TenancyCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem;

@Slf4j
@RestControllerAdvice(basePackages = "knemognition.heartauth.orchestrator.admin.interfaces.rest")
public class AdminExceptionHandler {
    @ExceptionHandler(TenancyCreationException.class)
    public ProblemDetail handleNoActiveDevice(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No active device for given user.", req, ex);
    }


}