package com.foodify.server.modules.auth.api;

import com.foodify.server.modules.auth.application.PhoneNumberUtils;
import com.foodify.server.modules.auth.dto.*;
import com.foodify.server.modules.auth.security.DriverAuthenticationDetails;
import com.foodify.server.modules.delivery.application.DriverSessionService;
import com.foodify.server.modules.delivery.domain.DriverSession;
import com.foodify.server.modules.delivery.domain.DriverSessionTerminationReason;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.RestaurantBasicInfoDto;
import com.foodify.server.modules.restaurants.mapper.RestaurantMapper;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

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
            if (user.getRole() == Role.RESTAURANT_ADMIN || user.getRole() == Role.RESTAURANT_CASHIER) {
                Restaurant restaurant = restaurantRepository.getRestaurantByAdmin((RestaurantAdmin) user);
                RestaurantBasicInfoDto restaurantDto = restaurantMapper.toBasicInfoDto(restaurant);
                return ResponseEntity.ok(Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken,
                        "user", user,
                        "restaurant", restaurantDto
                ));
            }

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
