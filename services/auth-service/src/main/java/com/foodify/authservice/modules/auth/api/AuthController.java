package com.foodify.authservice.modules.auth.api;

import com.foodify.authservice.modules.auth.application.PhoneNumberUtils;
import com.foodify.authservice.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.authservice.modules.auth.dto.LoginRequest;
import com.foodify.authservice.modules.auth.dto.RegisterRequest;
import com.foodify.authservice.modules.auth.security.JwtService;
import com.foodify.authservice.modules.delivery.client.DriverSessionClient;
import com.foodify.authservice.modules.identity.domain.AuthProvider;
import com.foodify.authservice.modules.identity.domain.Client;
import com.foodify.authservice.modules.identity.domain.Driver;
import com.foodify.authservice.modules.identity.domain.Role;
import com.foodify.authservice.modules.identity.domain.User;
import com.foodify.authservice.modules.identity.repository.ClientRepository;
import com.foodify.authservice.modules.identity.repository.DriverRepository;
import com.foodify.authservice.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DriverSessionClient driverSessionClient;

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.US);
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
            if (!StringUtils.hasText(user.getPhoneNumber()) && StringUtils.hasText(phone)) {
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

        if (StringUtils.hasText(phone) && clientRepository.existsByPhoneNumber(phone)) {
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

        String sessionToken = driverSessionClient.startSession(driver.getId(), request.getDeviceId());

        String accessToken = jwtService.generateAccessToken(user, sessionToken);
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
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("success", false, "message", "Email already in use"));
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
}
