package knemognition.heartauth.orchestrator.internal.config.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(FcmProperties.class)
public class FcmConfig {
    private static final String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    @Bean
    GoogleCredentials fcmCredentials(FcmProperties props, ResourceLoader loader) throws Exception {
        String loc = props.getCredentialsPath(); // "classpath:..." or absolute path; can be null to use ADC
        if (loc != null && !loc.isBlank()) {
            Resource res = loader.getResource(loc);
            if (!res.exists()) throw new FileNotFoundException("FCM credentials not found at: " + loc);
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

    @Bean(name = "fcmProjectId")
    String fcmProjectId(GoogleCredentials creds, Environment env, HttpClient http) {
        if (creds instanceof ServiceAccountCredentials sac && sac.getProjectId() != null) {
            return sac.getProjectId();
        }
        for (String key : new String[]{"FIREBASE_PROJECT_ID", "GOOGLE_CLOUD_PROJECT", "GCLOUD_PROJECT", "GCP_PROJECT"}) {
            String v = env.getProperty(key);
            if (v != null && !v.isBlank()) return v;
        }
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create("http://169.254.169.254/computeMetadata/v1/project/project-id"))
                    .timeout(Duration.ofMillis(500))
                    .header("Metadata-Flavor", "Google")
                    .GET().build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && res.body() != null && !res.body().isBlank()) {
                return res.body().trim();
            }
        } catch (Exception ignored) { }

        throw new IllegalStateException("""
      Could not resolve FCM project id. Provide one of:
        • Service Account JSON with 'project_id'
        • Env FIREBASE_PROJECT_ID / GOOGLE_CLOUD_PROJECT / GCLOUD_PROJECT / GCP_PROJECT
        • Run on GCP (metadata server)
      """);
    }
}
