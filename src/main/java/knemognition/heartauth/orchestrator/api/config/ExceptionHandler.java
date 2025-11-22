package knemognition.heartauth.orchestrator.api.config;

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
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static knemognition.heartauth.orchestrator.shared.utils.ExceptionHandlingUtils.problem;

@Slf4j
@RestControllerAdvice()
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(NoChallengeException.class)
    public ProblemDetail handleNoChallenge(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No challenge found.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoPairingException.class)
    public ProblemDetail handleNoPairing(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No pairing found.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ChallengeFailedException.class)
    public ProblemDetail handleChallengeFailed(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No challenge found.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.FORBIDDEN, "Invalid token.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DeviceEnrichException.class)
    public ProblemDetail handleDeviceEnrich(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Device data enrich failed.", req, ex);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(NonceValidationException.class)
    public ProblemDetail handleNonceValidation(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Nonce validation failed.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PemParsingException.class)
    public ProblemDetail handlePemParsing(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Pem format invalid.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoActiveDeviceException.class)
    public ProblemDetail handleNoActiveDevice(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "No active device for given user.", req, ex);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FirebaseSendException.class)
    public ProblemDetail handleFcmSend(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_GATEWAY, "Failed to send notification via FCM.", req, ex);
    }
}