package zpi.heartAuth.orchestrator.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableConfigurationProperties(FcmProperties.class)
public class FcmConfig {
    private static final String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    @Bean
    GoogleCredentials fcmCredentials(FcmProperties props, ResourceLoader loader) throws Exception {
        String loc = props.getCredentialsPath(); // e.g. "classpath:fcm/service-account.json" or "/abs/path.json"
        if (loc != null && !loc.isBlank()) {
            Resource res = loader.getResource(loc);
            if (!res.exists()) {
                throw new FileNotFoundException("FCM credentials not found at: " + loc);
            }
            try (InputStream in = res.getInputStream()) {
                return GoogleCredentials.fromStream(in).createScoped(SCOPE);
            }
        }
        return GoogleCredentials.getApplicationDefault().createScoped(SCOPE);
    }

    @Bean
    HttpClient fcmHttpClient() {
        return HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }
}
