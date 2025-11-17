package com.foodify.server.modules.admin.auth.api;

import com.foodify.server.modules.admin.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.common.util.AuthenticationUtil;
import com.foodify.server.modules.common.util.CookieUtil;
import com.foodify.server.modules.common.util.StringUtil;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://yourdomain.com"}, allowCredentials = "true")
public class AdminAuthController {
    
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String COOKIE_PATH = "/api/auth/admin";
    private static final int REFRESH_TOKEN_DURATION_DAYS = 7;
    private static final int REFRESH_TOKEN_MAX_AGE = REFRESH_TOKEN_DURATION_DAYS * 24 * 60 * 60;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String email = StringUtil.normalizeEmail(request.getEmail());
        if (!StringUtils.hasText(email) || !StringUtils.hasText(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Email and password are required"));
        }

        return userRepository.findByEmail(email)
                .map(user -> authenticateUser(user, request.getPassword(), response))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Invalid email or password")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(Authentication authentication, HttpServletResponse response) {
        AuthenticationUtil.extractUserId(authentication);
        response.addCookie(CookieUtil.createExpiredCookie(REFRESH_TOKEN_COOKIE_NAME, COOKIE_PATH));
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.extractCookieValue(request, REFRESH_TOKEN_COOKIE_NAME)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Refresh token not found"));
        }

        try {
            Claims claims = jwtService.parseRefreshToken(refreshToken);

            if (jwtService.isRefreshTokenExpired(refreshToken)) {
                response.addCookie(CookieUtil.createExpiredCookie(REFRESH_TOKEN_COOKIE_NAME, COOKIE_PATH));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Refresh token expired"));
            }

            Long userId = Long.valueOf(claims.getSubject());
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                response.addCookie(CookieUtil.createExpiredCookie(REFRESH_TOKEN_COOKIE_NAME, COOKIE_PATH));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("User not found"));
            }

            String newAccessToken = jwtService.generateAccessToken(user);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (Exception e) {
            response.addCookie(CookieUtil.createExpiredCookie(REFRESH_TOKEN_COOKIE_NAME, COOKIE_PATH));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid refresh token"));
        }
    }

    private ResponseEntity<Map<String, Object>> authenticateUser(User user, String password, HttpServletResponse response) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid email or password"));
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        response.addCookie(CookieUtil.createSecureCookie(
                REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                COOKIE_PATH,
                REFRESH_TOKEN_MAX_AGE
        ));

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "user", user
        ));
    }

    private Map<String, Object> createErrorResponse(String message) {
        return Map.of("success", false, "message", message);
    }
}