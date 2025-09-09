package knemognition.heartauth.orchestrator.shared.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionHandlingUtils {

    public static final URI TYPE_GENERIC = URI.create("urn:problem:generic-error");


    public static ProblemDetail problem(HttpStatus status,
                                        String title,
                                        HttpServletRequest req,
                                        Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setType(TYPE_GENERIC);
        pd.setTitle(title);

        if (status.is5xxServerError()) {
            pd.setDetail("An unexpected error occurred.");
        } else {
            pd.setDetail(ex.getMessage());
        }
        pd.setProperty("timestamp", OffsetDateTime.now(ZoneOffset.UTC).toString());


        if (status.is5xxServerError()) {
            log.error("{} {} -> {}", req.getMethod(), req.getRequestURI(), ex.toString(), ex);
        } else {
            log.warn("{} {} -> {} {}", req.getMethod(), req.getRequestURI(), status.value(), title);
        }
        return pd;
    }

}
