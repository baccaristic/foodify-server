package com.foodify.identityservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "identity.tokens")
public class OidcTokenProperties {
    private Duration accessTtl = Duration.ofHours(24);
    private Duration refreshTtl = Duration.ofDays(7);
    private String accessSecret = "Zm9vZGlmeS1hY2Nlc3Mtc2VjcmV0LXNob3VsZC1iZS1hdC1sZWFzdC0zMi1ieXRlcw==";
    private String refreshSecret = "Zm9vZGlmeS1yZWZyZXNoLXNlY3JldC1zaG91bGQtYmUtYXQtbGVhc3QtMzItYnl0ZXM=";
    private String issuer = "https://identity.foodify.local";
    private String audience = "foodify-platform";
    private String scope = "openid profile email";
    private String tokenType = "Bearer";

    public Duration getAccessTtl() {
        return accessTtl;
    }

    public void setAccessTtl(Duration accessTtl) {
        this.accessTtl = accessTtl;
    }

    public Duration getRefreshTtl() {
        return refreshTtl;
    }

    public void setRefreshTtl(Duration refreshTtl) {
        this.refreshTtl = refreshTtl;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getRefreshSecret() {
        return refreshSecret;
    }

    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
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
}
