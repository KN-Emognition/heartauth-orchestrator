package knemognition.heartauth.orchestrator.shared.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlingUtilsTest {

    @Test
    void shouldReturnProblemDetailForClientError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getRequestURI()).thenReturn("/resource");

        Exception ex = new IllegalArgumentException("bad request");
        ProblemDetail detail = ExceptionHandlingUtils.problem(HttpStatus.BAD_REQUEST, "Bad Request", request, ex);

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(detail.getDetail()).isEqualTo("bad request");
        assertThat(detail.getTitle()).isEqualTo("Bad Request");
        assertThat(detail.getProperties()).containsKey("timestamp");
        assertThat(detail.getType()).isEqualTo(ExceptionHandlingUtils.TYPE_GENERIC);
    }

    @Test
    void shouldMaskDetailForServerError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getMethod()).thenReturn("POST");
        Mockito.when(request.getRequestURI()).thenReturn("/internal");

        ProblemDetail detail = ExceptionHandlingUtils.problem(HttpStatus.INTERNAL_SERVER_ERROR, "Oops",
                request, new RuntimeException("secret"));

        assertThat(detail.getDetail()).isEqualTo("An unexpected error occurred.");
    }
}
