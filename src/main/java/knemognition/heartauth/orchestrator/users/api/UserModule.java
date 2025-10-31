package knemognition.heartauth.orchestrator.users.api;

import org.springframework.modulith.NamedInterface;

import java.util.List;
import java.util.Optional;

@NamedInterface
public interface UserModule {

    boolean checkIfUserExists(IdentifiableUserCmd user);

    Optional<UserRead> getUser(IdentifiableUserCmd user);

    UserRead saveUserDevice(SaveUserDeviceCmd cmd);

    List<DeviceRead> getUserDevices(IdentifiableUserCmd user);
}
