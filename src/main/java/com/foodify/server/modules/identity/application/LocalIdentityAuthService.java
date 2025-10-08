package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.auth.application.PhoneNumberUtils;
import com.foodify.server.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.RefreshTokenRequest;
import com.foodify.server.modules.auth.dto.RegisterRequest;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.api.CompleteRegistrationResponse;
import com.foodify.server.modules.identity.api.GoogleRegisterResponse;
import com.foodify.server.modules.identity.api.IdentityHeartbeatResponse;
import com.foodify.server.modules.identity.api.IdentityUserProfile;
import com.foodify.server.modules.identity.api.LoginResponse;
import com.foodify.server.modules.identity.api.TokenRefreshResponse;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class LocalIdentityAuthService implements IdentityAuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public GoogleRegisterResponse registerWithGoogle(GoogleRegisterRequest request) {
        if (!StringUtils.hasText(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
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
            return new GoogleRegisterResponse(
                    user.getEmail(),
                    user.getName(),
                    user.getGoogleId(),
                    jwtService.generateAccessToken(user),
                    jwtService.generateRefreshToken(user)
            );
        }

        if (clientRepository.existsByPhoneNumber(phone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already registered");
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

        return new GoogleRegisterResponse(
                saved.getEmail(),
                saved.getName(),
                saved.getGoogleId(),
                jwtService.generateAccessToken(saved),
                jwtService.generateRefreshToken(saved)
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                mapUser(user)
        );
    }

    @Override
    public LoginResponse driverLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (user.getRole() != Role.DRIVER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Driver role required");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new LoginResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                mapUser(user)
        );
    }

    @Override
    public CompleteRegistrationResponse completeRegistration(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
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

        return new CompleteRegistrationResponse(
                mapUser(saved),
                jwtService.generateAccessToken(saved)
        );
    }

    @Override
    public IdentityHeartbeatResponse heartbeat(String bearerToken) {
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No token");
        }

        String token = bearerToken.substring(7);
        if (jwtService.isAccessTokenExpired(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        return new IdentityHeartbeatResponse("active", null);
    }

    @Override
    public TokenRefreshResponse refresh(RefreshTokenRequest request) {
        if (request == null || !StringUtils.hasText(request.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        }

        String refreshToken = request.getRefreshToken();

        try {
            Claims claims = jwtService.parseRefreshToken(refreshToken);
            if (jwtService.isRefreshTokenExpired(refreshToken)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
            }

            Long userId = Long.valueOf(claims.getSubject());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

            return new TokenRefreshResponse(jwtService.generateAccessToken(user));
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", ex);
        }
    }

    private IdentityUserProfile mapUser(User user) {
        String phone = null;
        String address = null;
        Boolean phoneVerified = null;
        Boolean emailVerified = null;
        String googleId = null;

        if (user instanceof Client client) {
            phone = client.getPhoneNumber();
            address = client.getAddress();
            phoneVerified = client.getPhoneVerified();
            emailVerified = client.getEmailVerified();
            googleId = client.getGoogleId();
        } else if (user instanceof Driver driver) {
            phone = driver.getPhone();
        }

        return new IdentityUserProfile(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole() != null ? user.getRole().name() : null,
                phone,
                address,
                emailVerified,
                phoneVerified,
                null,
                googleId
        );
    }
}
