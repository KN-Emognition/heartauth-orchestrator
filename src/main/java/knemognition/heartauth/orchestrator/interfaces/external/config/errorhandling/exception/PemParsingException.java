package knemognition.heartauth.orchestrator.interfaces.external.config.errorhandling.exception;

public class PemParsingException extends RuntimeException {
    public PemParsingException(String message) {
        super(message);
    }
}
