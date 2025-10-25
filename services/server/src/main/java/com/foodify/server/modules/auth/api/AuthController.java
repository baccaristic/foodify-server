package com.foodify.server.modules.auth.api;

import com.foodify.server.modules.auth.dto.UpdateClientProfileRequest;
import com.foodify.server.modules.auth.dto.UpdateDriverProfileRequest;
import com.foodify.server.modules.auth.security.DriverAuthenticationDetails;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.delivery.application.DriverSessionService;
import com.foodify.server.modules.delivery.domain.DriverSessionTerminationReason;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import com.foodify.server.modules.media.ImageStorageClient;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;

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
    private final UserDeviceService userDeviceService;
    private final ImageStorageClient imageStorageClient;

    private Long extractUserId(Authentication authentication) {
        return Long.parseLong((String) authentication.getPrincipal());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.US);
    }

    private LocalDate parseDateOfBirthOrThrow(String value) {
        if (!StringUtils.hasText(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of birth is required");
        }
        try {
            LocalDate date = LocalDate.parse(value.trim());
            if (date.isAfter(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date of birth cannot be in the future");
            }
            return date;
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date of birth format. Expected ISO date (YYYY-MM-DD)");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PutMapping("/client/profile")
    public ResponseEntity<?> updateClientProfile(@RequestBody UpdateClientProfileRequest request,
                                                 Authentication authentication) {
        Long userId = extractUserId(authentication);
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        boolean updated = false;

        if (StringUtils.hasText(request.getName())) {
            String name = request.getName().trim();
            if (!name.equals(client.getName())) {
                client.setName(name);
                updated = true;
            }
        }

        if (StringUtils.hasText(request.getEmail())) {
            String email = normalizeEmail(request.getEmail());
            if (!StringUtils.hasText(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            }
            String currentEmail = client.getEmail();
            boolean emailChanged = currentEmail == null || !currentEmail.equalsIgnoreCase(email);
            if (emailChanged) {
                if (userRepository.existsByEmail(email)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }
                client.setEmail(email);
                client.setEmailVerified(false);
                updated = true;
            }
        }

        if (StringUtils.hasText(request.getDateOfBirth())) {
            LocalDate dateOfBirth = parseDateOfBirthOrThrow(request.getDateOfBirth());
            if (!dateOfBirth.equals(client.getDateOfBirth())) {
                client.setDateOfBirth(dateOfBirth);
                updated = true;
            }
        }

        if (StringUtils.hasText(request.getNewPassword())) {
            if (!StringUtils.hasText(request.getCurrentPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required to change the password");
            }
            if (!StringUtils.hasText(client.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password updates are not available for this account");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), client.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
            }
            client.setPassword(passwordEncoder.encode(request.getNewPassword()));
            updated = true;
        }

        if (updated) {
            clientRepository.save(client);
        }

        Map<String, Object> userPayload = Map.of(
                "id", client.getId(),
                "name", client.getName(),
                "email", client.getEmail(),
                "phone", client.getPhoneNumber(),
                "emailVerified", client.getEmailVerified(),
                "phoneVerified", client.getPhoneVerified(),
                "role", client.getRole(),
                "dateOfBirth", client.getDateOfBirth()
        );

        return ResponseEntity.ok(Map.of("success", true, "user", userPayload));
    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/driver/profile")
    public ResponseEntity<?> updateDriverProfile(@RequestBody UpdateDriverProfileRequest request,
                                                 Authentication authentication) {
        Long userId = extractUserId(authentication);
        Driver driver = driverRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

        boolean updated = false;

        if (StringUtils.hasText(request.getName())) {
            String name = request.getName().trim();
            if (!name.equals(driver.getName())) {
                driver.setName(name);
                updated = true;
            }
        }

        if (StringUtils.hasText(request.getEmail())) {
            String email = normalizeEmail(request.getEmail());
            if (!StringUtils.hasText(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            }
            String currentEmail = driver.getEmail();
            boolean emailChanged = currentEmail == null || !currentEmail.equalsIgnoreCase(email);
            if (emailChanged) {
                if (userRepository.existsByEmail(email)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }
                driver.setEmail(email);
                updated = true;
            }
        }

        if (StringUtils.hasText(request.getNewPassword())) {
            if (!StringUtils.hasText(request.getCurrentPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required to change the password");
            }
            if (!StringUtils.hasText(driver.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password updates are not available for this account");
            }
            if (!passwordEncoder.matches(request.getCurrentPassword(), driver.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
            }
            driver.setPassword(passwordEncoder.encode(request.getNewPassword()));
            updated = true;
        }

        if (updated) {
            driverRepository.save(driver);
        }

        Map<String, Object> userPayload = Map.of(
                "id", driver.getId(),
                "name", driver.getName(),
                "email", driver.getEmail(),
                "phone", driver.getPhone(),
                "role", driver.getRole()
        );

        return ResponseEntity.ok(Map.of("success", true, "user", userPayload));
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Void> getImage(@PathVariable String filename) {
        if (!StringUtils.hasText(filename)) {
            return ResponseEntity.badRequest().build();
        }

        String imageUrl = imageStorageClient.resolvePublicUrl(filename);
        if (!StringUtils.hasText(imageUrl)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(imageUrl))
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/client/logout")
    public ResponseEntity<?> logoutClient(Authentication authentication) {
        Long userId = extractUserId(authentication);
        userDeviceService.deleteDevicesForUser(userId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PostMapping("/driver/logout")
    public ResponseEntity<?> logoutDriver(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        Object details = authentication.getDetails();
        if (details instanceof DriverAuthenticationDetails driverDetails) {
            driverSessionService.endSessionByToken(driverDetails.sessionToken(), DriverSessionTerminationReason.LOGOUT);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Driver session not found");
        }

        Long userId = extractUserId(authentication);
        userDeviceService.deleteDevicesForUser(userId);

        return ResponseEntity.ok(Map.of("success", true));
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
