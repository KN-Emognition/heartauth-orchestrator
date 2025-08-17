package zpi.heartAuth.orchestrator.pairDevice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import zpi.heartAuth.orchestrator.config.fcm.FcmProperties;
import org.springframework.stereotype.Service;
import zpi.heartAuth.orchestrator.pairDevice.api.FcmService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {

    private final FcmProperties props;
    private final ObjectMapper mapper;
    private final HttpClient http;
    private final GoogleCredentials creds;

    /**
     * Send a notification to a single device token. Returns FCM message name.
     */
    @SneakyThrows
    @Override
    public String sendToToken(String token, String title, String body, Map<String, String> data) {
        var payload = Map.of(
                "message", Map.of(
                        "token", token,
                        "notification", Map.of("title", title, "body", body),
                        "data", data == null ? Map.<String, String>of() : data
                ),
                "validate_only", props.isDryRun()
        );
        return callFcm(payload);
    }

    @SneakyThrows
    private String callFcm(Object payload) {
        String url = "https://fcm.googleapis.com/v1/projects/" + props.getProjectId() + "/messages:send";
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
            log.warn("FCM error {}: {}", res.statusCode(), res.body());
            throw new RuntimeException("FCM error %d: %s".formatted(res.statusCode(), res.body()));
        }
        return mapper.readTree(res.body()).path("name").asText();
    }

    @SneakyThrows
    private synchronized String accessToken() {
        creds.refreshIfExpired();
        return creds.getAccessToken().getTokenValue();
    }
}
