package zpi.heartAuth.orchestrator.pairDevice.api;

import lombok.SneakyThrows;

import java.util.Map;

public interface FcmService {
    @SneakyThrows
    String sendToToken(String token, String title, String body, Map<String, String> data);
}
