package com.foodify.server.modules.identity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "identity.tokens")
public class IdentityTokenProperties {

    private final TokenConfig access = new TokenConfig(Duration.ofHours(24),
            "Zm9vZGlmeS1hY2Nlc3Mtc2VjcmV0LXNob3VsZC1iZS1hdC1sZWFzdC0zMi1ieXRlcw==");
    private final TokenConfig refresh = new TokenConfig(Duration.ofDays(7),
            "Zm9vZGlmeS1yZWZyZXNoLXNlY3JldC1zaG91bGQtYmUtYXQtbGVhc3QtMzItYnl0ZXM=");
    private String issuer = "https://identity.foodify.local";
    private String audience = "foodify-platform";
    private String scope = "openid profile email";
    private String tokenType = "Bearer";

    public TokenConfig getAccess() {
        return access;
    }

    public TokenConfig getRefresh() {
        return refresh;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
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
