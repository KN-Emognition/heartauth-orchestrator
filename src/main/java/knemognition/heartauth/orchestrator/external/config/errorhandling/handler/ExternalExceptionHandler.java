package knemognition.heartauth.orchestrator.external.config.errorhandling.handler;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.challenges.app.exceptions.ChallengeFailedException;
import knemognition.heartauth.orchestrator.challenges.app.exceptions.NoChallengeException;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.*;
import knemognition.heartauth.orchestrator.security.app.exception.NonceValidationException;
import knemognition.heartauth.orchestrator.security.app.exception.PemParsingException;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem;

@Slf4j
@RestControllerAdvice(basePackages = "knemognition.heartauth.orchestrator.external.interfaces.rest")
public class ExternalExceptionHandler {
    @ExceptionHandler(NoChallengeException.class)
    public ProblemDetail handleNoChallenge(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No challenge found.", req, ex);
    }

    @ExceptionHandler(NoPairingException.class)
    public ProblemDetail handleNoPairing(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No pairing found.", req, ex);
    }

    @ExceptionHandler(ChallengeFailedException.class)
    public ProblemDetail handleChallengeFailed(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No challenge found.", req, ex);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.FORBIDDEN, "Invalid token.", req, ex);
    }

    @ExceptionHandler(DeviceEnrichException.class)
    public ProblemDetail handleDeviceEnrich(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Device data enrich failed.", req, ex);
    }

    @ExceptionHandler(StatusServiceException.class)
    public ProblemDetail handleStatusService(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Status operation for given flow failed.", req, ex);
    }

    @ExceptionHandler(NonceValidationException.class)
    public ProblemDetail handleNonceValidation(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Nonce validation failed.", req, ex);
    }

    @ExceptionHandler(PemParsingException.class)
    public ProblemDetail handlePemParsing(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Pem format invalid.", req, ex);
    }
}