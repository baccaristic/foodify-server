package com.foodify.server.controller;

import com.foodify.server.dto.*;
import com.foodify.server.enums.AuthProvider;
import com.foodify.server.enums.Role;
import com.foodify.server.models.Client;
import com.foodify.server.models.User;
import com.foodify.server.repository.ClientRepository;
import com.foodify.server.repository.UserRepository;
import com.foodify.server.security.JwtService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cache-phone")
    public ResponseEntity<?> cachePhone(@RequestBody PhoneRequest request, HttpSession session) {
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Phone is required"));
        }
        session.setAttribute("pendingPhone", request.getPhone());
        return ResponseEntity.ok(Map.of("success", true, "message", "Phone cached successfully"));
    }

    @PostMapping(value = "/verify-phone",  consumes = {"application/json"})
    public ResponseEntity<?> verifyPhone(@RequestBody PhoneRequest request) {
        return ResponseEntity.ok(Map.of("success", true, "message", "Phone verified successfully"));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerificationRequest request) {
        return ResponseEntity.ok(Map.of("success", true, "message", "Email verified successfully"));
    }

    @PostMapping("/google")
    public ResponseEntity<?> registerWithGoogle(@RequestBody GoogleRegisterRequest request, HttpSession session) {
        String phone = (String) session.getAttribute("pendingPhone");
        if (phone == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Phone number not cached"));
        }

        Optional<Client> existing = clientRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            Client user = existing.get();
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(Map.of("email", user.getEmail(), "name", user.getName(), "googleId", user.getGoogleId(), "token", token));
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

        String token = jwtService.generateToken(saved);

        return ResponseEntity.ok(Map.of(
                "email", saved.getEmail(),
                "name", saved.getName(),
                "googleId", saved.getGoogleId(),
                "token", token
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid email or password"));
        }
        User user = userRepository.findByEmail(request.getEmail()).get();
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(Map.of("success", true, "token", token));
        }
        else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid email or password"));
        }
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
        String token = jwtService.generateToken(saved);

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
}
