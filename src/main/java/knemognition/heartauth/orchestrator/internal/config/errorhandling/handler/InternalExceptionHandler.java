package knemognition.heartauth.orchestrator.internal.config.errorhandling.handler;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.ApiKeyException;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.FirebaseSendException;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import static knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem;

@Slf4j
@RestControllerAdvice(basePackages = "knemognition.heartauth.orchestrator.internal.interfaces.rest")
public class InternalExceptionHandler {
    @ExceptionHandler(NoActiveDeviceException.class)
    public ProblemDetail handleNoActiveDevice(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No active device for given user.", req, ex);
    }

    @ExceptionHandler(FirebaseSendException.class)
    public ProblemDetail handleFcmSend(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_GATEWAY, "Failed to send notification via FCM.", req, ex);
    }

    @ExceptionHandler(StatusServiceException.class)
    public ProblemDetail handleStatusService(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Status operation for given flow failed.", req, ex);
    }

    @ExceptionHandler(ApiKeyException.class)
    public ProblemDetail handleApiKey(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Failed to validate with API Key.", req, ex);
    }
}