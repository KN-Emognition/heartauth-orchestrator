package knemognition.heartauth.orchestrator.user.app.handlers;


import knemognition.heartauth.orchestrator.tenant.api.TenantApi;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckIfUserExistsHandler {
    private final TenantApi tenantApi;
    private final UserRepository userRepository;

    public boolean handle(IdentifiableUserCmd user) {
        var tenant = tenantApi.get(user.getTenantId())
                .orElseThrow();
        return userRepository.existsByTenantIdAndUserId(tenant.getId(), user.getTenantId());
    }
}
