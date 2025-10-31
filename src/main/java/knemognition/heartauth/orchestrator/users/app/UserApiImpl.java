package knemognition.heartauth.orchestrator.users.app;

import knemognition.heartauth.orchestrator.users.api.*;
import knemognition.heartauth.orchestrator.users.app.handlers.CheckIfUserExistsHandler;
import knemognition.heartauth.orchestrator.users.app.handlers.GetUserDeviceHandler;
import knemognition.heartauth.orchestrator.users.app.handlers.GetUserHandler;
import knemognition.heartauth.orchestrator.users.app.handlers.SaveUserDeviceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserModule {

    private final CheckIfUserExistsHandler checkIfUserExistsHandler;
    private final GetUserDeviceHandler getUserDeviceHandler;
    private final GetUserHandler getUserIdHandler;
    private final SaveUserDeviceHandler saveUserDeviceHandler;

    @Override
    public boolean checkIfUserExists(IdentifiableUserCmd user) {
        return checkIfUserExistsHandler.handle(user);
    }

    @Override
    public Optional<UserRead> getUser(IdentifiableUserCmd user) {
        return getUserIdHandler.handle(user);
    }

    @Override
    public UserRead saveUserDevice(SaveUserDeviceCmd cmd) {
        return saveUserDeviceHandler.handle(cmd);
    }

    @Override
    public List<DeviceRead> getUserDevices(IdentifiableUserCmd user) {
        return getUserDeviceHandler.handle(user);
    }


}
