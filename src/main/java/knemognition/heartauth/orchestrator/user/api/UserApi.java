package knemognition.heartauth.orchestrator.user.api;

import java.util.List;

public interface UserApi {

    boolean checkIfUserExists(IdentifiableUserCmd user);

    List<DeviceRead> getUserDevices(IdentifiableUserCmd user);
}
