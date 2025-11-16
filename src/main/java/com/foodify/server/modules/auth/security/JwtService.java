package com.foodify.server.modules.auth.security;

import com.foodify.server.modules.identity.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Key refreshSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Separate key for refresh

    // === Access Token ===
    public String generateAccessToken(User user) {
        return generateAccessToken(user, null);
    }

    public String generateAccessToken(User user, String sessionToken) {
        long expirationTimeMillis = 1000 * 60; // 24 hours
        var builder = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));

        if (sessionToken != null) {
            builder.claim("sessionToken", sessionToken);
        }

        return builder.signWith(secretKey).compact();
    }

    // === Refresh Token ===
    public String generateRefreshToken(User user) {
        long expirationTimeMillis = 1000L * 60 * 60 * 24 * 7; // 7 days
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
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
