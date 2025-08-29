package knemognition.heartauth.orchestrator.internal.gateways.messaging.fcm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import knemognition.heartauth.orchestrator.internal.config.errorhandling.exception.FcmSendException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import knemognition.heartauth.orchestrator.internal.config.fcm.FcmProperties;
import org.springframework.stereotype.Service;
import knemognition.heartauth.orchestrator.internal.app.ports.out.FcmSender;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmSenderImpl implements FcmSender {

    private final FcmProperties props;
    private final ObjectMapper mapper;
    private final HttpClient http;
    private final GoogleCredentials creds;


    private static final String FCM_URL_FMT = "https://fcm.googleapis.com/v1/projects/%s/messages:send";

    /**
     * Send a notification to a single device token. Returns FCM message name.
     */
    @Override
    public void sendData(String token, Map<String, String> data, Duration ttl) {
        var payload = buildDataOnlyPayload(token, data, ttl);
        callFcm(payload);
    }

    @SneakyThrows
    private void callFcm(Object payload) {
        String url = FCM_URL_FMT.formatted(props.getProjectId());
        String json = mapper.writeValueAsString(payload);
        String bearer = accessToken();

        var req = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("Authorization", "Bearer " + bearer)
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        var res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) {
//            log.warn("FCM error {}: {}", res.statusCode(), res.body());
            throw new FcmSendException("FCM error %d: %s".formatted(res.statusCode(), res.body()));
        }
    }

    private Map<String, Object> buildDataOnlyPayload(String token, Map<String, String> data, Duration ttl) {
        var ttlStr = ttl == null ? null : ttl.toSeconds() + "s";
        long expEpoch = ttl == null ? 0 : Instant.now().plus(ttl).getEpochSecond();

        return Map.of(
                "message", Map.of(
                        "token", token,
                        "data", data == null ? Map.<String, String>of() : data,
                        "notification", Map.of("title", "New login attempt", "body", "Authenticate yourself to complete the login process."),
                        "android", ttlStr == null ? Map.of("priority", "HIGH")
                                : Map.of("priority", "HIGH", "ttl", ttlStr),
                        "apns", ttlStr == null ? Map.of()
                                : Map.of("headers", Map.of("apns-expiration", String.valueOf(expEpoch)))
                ),
                "validate_only", props.isDryRun()
        );
    }

    @SneakyThrows
    private synchronized String accessToken() {
        creds.refreshIfExpired();
        return creds.getAccessToken().getTokenValue();
    }
}
