package com.foodify.server.modules.admin.auth.api;

import com.foodify.server.modules.admin.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://yourdomain.com"}, allowCredentials = "true")
public class AdminAuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_TOKEN_DURATION_DAYS = 7;

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong((String) authentication.getPrincipal());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.US);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/admin");
        cookie.setMaxAge(REFRESH_TOKEN_DURATION_DAYS * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private Cookie createExpiredRefreshTokenCookie() {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/admin");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String email = normalizeEmail(request.getEmail());
        if (!StringUtils.hasText(email) || !StringUtils.hasText(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Email and password are required"));
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid email or password"));
        }

        User user = optionalUser.get();

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            response.addCookie(createRefreshTokenCookie(refreshToken));

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "user", user
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Invalid email or password"));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutClient(Authentication authentication, HttpServletResponse response) {
        Long userId = extractUserId(authentication);
        response.addCookie(createExpiredRefreshTokenCookie());
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookie
        String refreshToken = extractRefreshTokenFromCookies(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Refresh token not found"));
        }

        try {
            Claims claims = jwtService.parseRefreshToken(refreshToken);

            if (jwtService.isRefreshTokenExpired(refreshToken)) {
                response.addCookie(createExpiredRefreshTokenCookie());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Refresh token expired"));
            }

            Long userId = Long.valueOf(claims.getSubject());
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                response.addCookie(createExpiredRefreshTokenCookie());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "User not found"));
            }

            String newAccessToken = jwtService.generateAccessToken(user);

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken
            ));

        } catch (Exception e) {
            response.addCookie(createExpiredRefreshTokenCookie());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid refresh token"));
        }
    }

}