package knemognition.heartauth.orchestrator.users.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.SaveUserDeviceCmd;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.app.mappers.UserMapper;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.UserEntity;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveUserDeviceHandler {

    private final TenantsModule tenantApi;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRead handle(SaveUserDeviceCmd payload) {
        var tenant = tenantApi.get(payload.getUser()
                        .getTenantId())
                .orElseThrow();

        UserEntity user = userMapper.toEntity(payload.getUser());
        user.setTenantId(tenant.getId());
        user.addDevice(userMapper.toEntity(payload.getDevice()));
        userRepository.save(user);
        return userMapper.toReadModel(user);
    }
}
