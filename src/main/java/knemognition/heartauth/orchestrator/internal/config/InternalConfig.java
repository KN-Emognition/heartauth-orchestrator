package knemognition.heartauth.orchestrator.internal.config;

import knemognition.heartauth.orchestrator.internal.InternalMarker;
import knemognition.heartauth.orchestrator.shared.SharedMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("internal")
@Configuration
@ComponentScan(basePackageClasses = { InternalMarker.class, SharedMarker.class })
public class InternalConfig {}
