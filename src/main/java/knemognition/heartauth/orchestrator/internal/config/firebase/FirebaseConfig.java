package knemognition.heartauth.orchestrator.internal.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
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
        GoogleCredentials creds;
        if (StringUtils.hasText(props.getCredentialsLocation())) {
            Resource r = resourceLoader.getResource(props.getCredentialsLocation());
            try (InputStream is = r.getInputStream()) {
                creds = GoogleCredentials.fromStream(is);
            }
        } else {
            creds = GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions.Builder b = FirebaseOptions.builder().setCredentials(creds);
        return FirebaseApp.initializeApp(b.build());
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }
}
