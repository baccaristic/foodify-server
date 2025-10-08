package com.foodify.server.modules.identity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "identity.tokens")
public class IdentityTokenProperties {

    private final TokenConfig access = new TokenConfig(Duration.ofHours(24),
            "Zm9vZGlmeS1hY2Nlc3Mtc2VjcmV0LXNob3VsZC1iZS1hdC1sZWFzdC0zMi1ieXRlcw==");
    private final TokenConfig refresh = new TokenConfig(Duration.ofDays(7),
            "Zm9vZGlmeS1yZWZyZXNoLXNlY3JldC1zaG91bGQtYmUtYXQtbGVhc3QtMzItYnl0ZXM=");

    public TokenConfig getAccess() {
        return access;
    }

    public TokenConfig getRefresh() {
        return refresh;
    }

    public static class TokenConfig {
        private Duration ttl;
        private String secret;

        public TokenConfig(Duration ttl, String secret) {
            this.ttl = ttl;
            this.secret = secret;
        }

        public Duration getTtl() {
            return ttl;
        }

        public void setTtl(Duration ttl) {
            this.ttl = ttl;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
}
