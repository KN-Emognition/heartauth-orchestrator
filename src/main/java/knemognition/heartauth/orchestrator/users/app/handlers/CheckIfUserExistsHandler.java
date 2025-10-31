package knemognition.heartauth.orchestrator.users.app.handlers;


import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckIfUserExistsHandler {
    private final TenantsModule tenantApi;
    private final UserRepository userRepository;

    public boolean handle(IdentifiableUserCmd user) {
        var tenant = tenantApi.get(user.getTenantId())
                .orElseThrow();
        return userRepository.existsByUserIdAndTenantId(user.getUserId(), tenant.getId());
    }
}
