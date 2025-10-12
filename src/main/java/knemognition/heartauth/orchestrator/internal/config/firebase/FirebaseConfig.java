package knemognition.heartauth.orchestrator.internal.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
@RequiredArgsConstructor
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;
    private final FirebaseProperties props;

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        GoogleCredentials credentials;
        if (StringUtils.hasText(props.getCredentialsLocation())) {
            Resource r = resourceLoader.getResource(props.getCredentialsLocation());
            try (InputStream is = r.getInputStream()) {
                credentials = GoogleCredentials.fromStream(is);
            }
        } else {
            credentials = GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions.Builder b = FirebaseOptions.builder()
                .setCredentials(credentials);
        return FirebaseApp.initializeApp(b.build());
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }
}
