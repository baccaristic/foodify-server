package com.foodify.server.modules.notifications.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@Slf4j
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        if (!StringUtils.hasText(credentialsPath)) {
            log.warn("GOOGLE_APPLICATION_CREDENTIALS not configured; skipping Firebase initialization");
            return;
        }

        Path path = Path.of(credentialsPath);
        if (!Files.exists(path)) {
            log.warn("Firebase credentials file not found at {}. Skipping initialization.", path);
            return;
        }

        try (FileInputStream serviceAccount = new FileInputStream(path.toFile())) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }
}
