package knemognition.heartauth.orchestrator.internal.config;

import knemognition.heartauth.orchestrator.internal.InternalMarker;
import knemognition.heartauth.orchestrator.shared.SharedMarker;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Spring {@link Configuration} class for the {@code internal} profile.
 * <p>
 * When the application is started with the {@code internal} Spring profile active,
 * this configuration becomes active and triggers component scanning for:
 * <ul>
 *   <li>all beans under the {@code knemognition.heartauth.orchestrator.internal} package
 *       (via the {@link InternalMarker} class marker)</li>
 *   <li>all beans under the {@code knemognition.heartauth.orchestrator.shared} package
 *       (via the {@link SharedMarker} class marker)</li>
 * </ul>
 */
@Profile(SpringProfiles.INTERNAL)
@Configuration
@ComponentScan(basePackageClasses = {InternalMarker.class, SharedMarker.class})
public class InternalConfig {
}
