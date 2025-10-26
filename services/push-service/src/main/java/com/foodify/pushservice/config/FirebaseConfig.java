package com.foodify.pushservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (!StringUtils.hasText(credentialsPath)) {
            log.warn("GOOGLE_APPLICATION_CREDENTIALS not configured; skipping Firebase initialization");
            throw new IllegalStateException("Firebase credentials are required for push notifications");
        }

        Path path = Path.of(credentialsPath);
        if (!Files.exists(path)) {
            log.warn("Firebase credentials file not found at {}. Skipping initialization.", path);
            throw new IllegalStateException("Firebase credentials file not found at " + path);
        }

        if (FirebaseApp.getApps().isEmpty()) {
            try (FileInputStream serviceAccount = new FileInputStream(path.toFile())) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        }

        return FirebaseMessaging.getInstance();
    }
}
