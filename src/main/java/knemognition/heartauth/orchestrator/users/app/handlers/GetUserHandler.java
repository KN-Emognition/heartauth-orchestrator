package knemognition.heartauth.orchestrator.users.app.handlers;


import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.app.mappers.UserMapper;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserHandler {
    private final TenantsModule tenantApi;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<UserRead> handle(IdentifiableUserCmd user) {
        var tenant = tenantApi.get(user.getTenantId())
                .orElseThrow();
        return userRepository.findByUserIdAndTenantId(user.getUserId(), tenant.getId())
                .map(userMapper::toReadModel);
    }
}
