package knemognition.heartauth.orchestrator.shared.config.rest;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.challenges.api.ChallengeFailedException;
import knemognition.heartauth.orchestrator.challenges.api.FirebaseSendException;
import knemognition.heartauth.orchestrator.challenges.api.NoChallengeException;
import knemognition.heartauth.orchestrator.pairings.api.DeviceEnrichException;
import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.security.api.InvalidTokenException;
import knemognition.heartauth.orchestrator.security.api.NonceValidationException;
import knemognition.heartauth.orchestrator.security.api.PemParsingException;
import knemognition.heartauth.orchestrator.users.api.NoActiveDeviceException;
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


    @ExceptionHandler(NonceValidationException.class)
    public ProblemDetail handleNonceValidation(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Nonce validation failed.", req, ex);
    }

    @ExceptionHandler(PemParsingException.class)
    public ProblemDetail handlePemParsing(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Pem format invalid.", req, ex);
    }

    @ExceptionHandler(NoActiveDeviceException.class)
    public ProblemDetail handleNoActiveDevice(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No active device for given user.", req, ex);
    }

    @ExceptionHandler(FirebaseSendException.class)
    public ProblemDetail handleFcmSend(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_GATEWAY, "Failed to send notification via FCM.", req, ex);
    }
}