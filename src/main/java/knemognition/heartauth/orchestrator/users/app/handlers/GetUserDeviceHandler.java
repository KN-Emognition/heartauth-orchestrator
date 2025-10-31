package knemognition.heartauth.orchestrator.users.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.app.mappers.UserMapper;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserDeviceHandler {
    private final TenantsModule tenantApi;
    private final UserMapper userMapper;
    private final DeviceRepository deviceRepository;

    public List<DeviceRead> handle(IdentifiableUserCmd user) {
        var tenant = tenantApi.get(user.getTenantId())
                .orElseThrow();
        return deviceRepository.findAllByAppUser_UserIdAndAppUser_TenantId(user.getUserId(), tenant.getId())
                .stream()
                .map(userMapper::toReadModel)
                .toList();
    }
}
