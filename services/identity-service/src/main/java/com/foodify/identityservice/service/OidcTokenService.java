package com.foodify.identityservice.service;

import com.foodify.identityservice.config.OidcTokenProperties;
import com.foodify.identityservice.domain.IdentityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class OidcTokenService {
    private final Key accessKey;
    private final Key refreshKey;
    private final OidcTokenProperties properties;

    public OidcTokenService(OidcTokenProperties properties) {
        this.properties = properties;
        this.accessKey = buildKey(properties.getAccessSecret());
        this.refreshKey = buildKey(properties.getRefreshSecret());
    }

    private Key buildKey(String secret) {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (IllegalArgumentException ex) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String generateAccessToken(IdentityUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(properties.getIssuer())
                .setAudience(properties.getAudience())
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("scope", properties.getScope())
                .claim("token_type", properties.getTokenType())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(properties.getAccessTtl())))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(IdentityUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(properties.getIssuer())
                .setAudience(properties.getAudience())
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(properties.getRefreshTtl())))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isRefreshTokenExpired(String token) {
        return parseRefreshToken(token).getExpiration().before(new Date());
    }

    public String getTokenType() {
        return properties.getTokenType();
    }

    public long getAccessExpiresIn() {
        return properties.getAccessTtl().toSeconds();
    }

    public String getScope() {
        return properties.getScope();
    }
}
