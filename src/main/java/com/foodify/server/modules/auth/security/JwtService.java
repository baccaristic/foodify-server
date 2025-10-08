package com.foodify.server.modules.auth.security;

import com.foodify.server.modules.identity.config.IdentityTokenProperties;
import com.foodify.server.modules.identity.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    private final Key secretKey;
    private final Key refreshSecretKey; // Separate key for refresh
    private final Duration accessTtl;
    private final Duration refreshTtl;

    public JwtService(IdentityTokenProperties properties) {
        this.secretKey = buildKey(properties.getAccess().getSecret(), "access");
        this.refreshSecretKey = buildKey(properties.getRefresh().getSecret(), "refresh");
        this.accessTtl = properties.getAccess().getTtl();
        this.refreshTtl = properties.getRefresh().getTtl();
    }

    private Key buildKey(String secret, String alias) {
        if (!StringUtils.hasText(secret)) {
            log.warn("Identity token {} secret is not configured; generating ephemeral key", alias);
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (IllegalArgumentException ex) {
            log.warn("Identity token {} secret is not valid Base64; using raw bytes", alias);
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    // === Access Token ===
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(accessTtl)))
                .signWith(secretKey)
                .compact();
    }

    // === Refresh Token ===
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(refreshTtl)))
                .signWith(refreshSecretKey)
                .compact();
    }

    // === Token Parsing ===
    public Claims parseAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // === Expiry Check ===
    public boolean isAccessTokenExpired(String token){
        return parseAccessToken(token).getExpiration().before(new Date());
    }

    public boolean isRefreshTokenExpired(String token){
        return parseRefreshToken(token).getExpiration().before(new Date());
    }
}
