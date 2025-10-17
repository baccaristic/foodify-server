package com.foodify.server.modules.auth.api;

import com.foodify.server.modules.auth.application.PhoneNumberUtils;
import com.foodify.server.modules.auth.dto.*;
import com.foodify.server.modules.delivery.application.DriverSessionService;
import com.foodify.server.modules.delivery.domain.DriverSession;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import com.foodify.server.modules.auth.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DriverRepository driverRepository;
    private final DriverSessionService driverSessionService;

    @PostMapping("/google")
    public ResponseEntity<?> registerWithGoogle(@RequestBody GoogleRegisterRequest request) {
        if (!StringUtils.hasText(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email is required"));
        }

        String phone = PhoneNumberUtils.normalize(request.getPhone());

        Optional<Client> existing = clientRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            Client user = existing.get();
            if (!StringUtils.hasText(user.getPhoneNumber())) {
                user.setPhoneNumber(phone);
                user.setPhoneVerified(true);
                clientRepository.save(user);
            }
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return ResponseEntity.ok(Map.of(
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "googleId", user.getGoogleId(),
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        }

        if (clientRepository.existsByPhoneNumber(phone)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("success", false, "message", "Phone number already registered"));
        }

        Client client = new Client();
        client.setEmail(request.getEmail());
        client.setName(request.getName());
        client.setGoogleId(request.getGoogleId());
        client.setPhoneNumber(phone);
        client.setPhoneVerified(true);
        client.setEmailVerified(true);
        client.setRole(Role.CLIENT);
        client.setAuthProvider(AuthProvider.GOOGLE);

        Client saved = clientRepository.save(client);

        String accessToken = jwtService.generateAccessToken(saved);
        String refreshToken = jwtService.generateRefreshToken(saved);

        return ResponseEntity.ok(Map.of(
                "email", saved.getEmail(),
                "name", saved.getName(),
                "googleId", saved.getGoogleId(),
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid email or password"));
        }

        User user = optionalUser.get();

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // Return AuthResponse style payload
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "user", user
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Invalid email or password"));
    }

    @PostMapping("/driver/login")
    public ResponseEntity<?> driverLogin(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid email or password"));
        }

        User user = optionalUser.get();

        if (user.getRole() != Role.DRIVER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Driver role required"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid email or password"));
        }

        Driver driver = driverRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Driver profile not found"));

        DriverSession session = driverSessionService.startSession(driver, request.getDeviceId());

        String accessToken = jwtService.generateAccessToken(user, session.getSessionToken());
        String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "user", user
        ));
    }


    @PostMapping("/register/complete")
    public ResponseEntity<?> completeRegistration(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("success", false, "message", "Email already in use"));
        }

        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhone());
        client.setAddress(request.getAddress());
        client.setRole(Role.CLIENT);
        client.setAuthProvider(request.isGoogleAccount() ? AuthProvider.GOOGLE : AuthProvider.LOCAL);
        client.setPhoneVerified(request.isPhoneVerified());
        client.setEmailVerified(request.isEmailVerified());

        if (!request.isGoogleAccount()) {
            client.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Client saved = clientRepository.save(client);
        String token = jwtService.generateAccessToken(saved);

        return ResponseEntity.ok(Map.of(
                "user", Map.of(
                        "id", saved.getId(),
                        "name", saved.getName(),
                        "email", saved.getEmail(),
                        "phone", saved.getPhoneNumber(),
                        "address", saved.getAddress(),
                        "role", saved.getRole().name(),
                        "verified", saved.getPhoneVerified() && saved.getEmailVerified(),
                        "emailVerified", saved.getEmailVerified(),
                        "phoneVerified", saved.getPhoneVerified()
                ),
                "token", token
        ));
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("uploads").resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/heart-beat")
    public ResponseEntity<?> checkSession(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "expired", "message", "No token"));
        }

        String token = authHeader.substring(7);

        if (jwtService.isAccessTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "expired", "message", "Token expired"));
        }

        return ResponseEntity.ok(Map.of("status", "active"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            // Parse and validate refresh token
            Claims claims = jwtService.parseRefreshToken(refreshToken);

            if (jwtService.isRefreshTokenExpired(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Refresh token expired"));
            }

            // Extract user ID
            Long userId = Long.valueOf(claims.getSubject());
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "User not found"));
            }

            // Generate new tokens
            String newAccessToken = jwtService.generateAccessToken(user);

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid refresh token"));
        }
    }

}
