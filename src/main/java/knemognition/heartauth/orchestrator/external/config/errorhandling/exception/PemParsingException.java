package knemognition.heartauth.orchestrator.external.config.errorhandling.exception;

public class PemParsingException extends RuntimeException {
    public PemParsingException(String message) {
        super(message);
    }
}
