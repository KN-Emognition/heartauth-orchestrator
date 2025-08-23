package knemognition.heartauth.orchestrator.internal.app.ports.out;

import knemognition.heartauth.orchestrator.internal.app.domain.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceDirectory {

    List<String> getActiveFcmTokens(String userId);
}
