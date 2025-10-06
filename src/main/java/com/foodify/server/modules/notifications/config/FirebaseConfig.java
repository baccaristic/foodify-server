package com.foodify.server.modules.notifications.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    private final String credentialsBase64;
    private final String credentialsPath;
    private final ResourceLoader resourceLoader;

    public FirebaseConfig(
            @Value("${firebase.credentials.base64:}") String credentialsBase64,
            @Value("${firebase.credentials.path:}") String credentialsPath,
            ResourceLoader resourceLoader
    ) {
        this.credentialsBase64 = credentialsBase64;
        this.credentialsPath = credentialsPath;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void initialize() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        try (InputStream serviceAccount = resolveCredentialsStream()) {
            if (serviceAccount == null) {
                log.info("Firebase credentials not configured; skipping Firebase initialization.");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    private InputStream resolveCredentialsStream() throws IOException {
        if (StringUtils.hasText(credentialsBase64)) {
            byte[] decoded = Base64.getDecoder().decode(credentialsBase64);
            return new ByteArrayInputStream(decoded);
        }

        if (StringUtils.hasText(credentialsPath)) {
            Resource resource = resourceLoader.getResource(credentialsPath);
            if (!resource.exists()) {
                throw new IOException("Firebase credentials file not found at " + credentialsPath);
            }
            return resource.getInputStream();
        }

        String envCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
        if (StringUtils.hasText(envCredentials)) {
            return new ByteArrayInputStream(envCredentials.getBytes(StandardCharsets.UTF_8));
        }

        return null;
    }
}
