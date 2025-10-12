package knemognition.heartauth.orchestrator.external;

import knemognition.heartauth.orchestrator.external.config.ExternalConfig;
import knemognition.heartauth.orchestrator.internal.config.InternalConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import test.config.HeartauthSpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("external")
class ExternalMarkerTest extends HeartauthSpringBootTest {
    @Autowired
    ApplicationContext ctx;

    @Test
    void contextLoads_withExternalProfile() {
        assertThat(ctx).isNotNull();
        assertThat(ctx.getBean(ExternalConfig.class)).isNotNull();
        assertThat(ctx.getBeanProvider(InternalConfig.class)
                .getIfAvailable()).isNull();
    }
}