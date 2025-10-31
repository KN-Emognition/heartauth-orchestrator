package knemognition.heartauth.orchestrator.user.app.handlers;

import knemognition.heartauth.orchestrator.tenant.api.TenantApi;
import knemognition.heartauth.orchestrator.user.api.DeviceRead;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.user.app.mappers.UserMapper;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserDeviceHandler {
    private final TenantApi tenantApi;
    private final UserMapper userMapper;
    private final DeviceRepository deviceRepository;

    public List<DeviceRead> handle(IdentifiableUserCmd user) {
        var tenant = tenantApi.get(user.getTenantId())
                .orElseThrow();
        return deviceRepository.findAllByTenantIdAndUserId(tenant.getId(), user.getTenantId())
                .stream()
                .map(userMapper::toReadModel)
                .toList();
    }
}
