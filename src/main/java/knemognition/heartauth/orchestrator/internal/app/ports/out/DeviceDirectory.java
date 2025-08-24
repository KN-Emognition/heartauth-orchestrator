package knemognition.heartauth.orchestrator.internal.app.ports.out;


import java.util.List;
import java.util.UUID;

public interface DeviceDirectory {

    List<String> getActiveFcmTokens(UUID userId);
}
