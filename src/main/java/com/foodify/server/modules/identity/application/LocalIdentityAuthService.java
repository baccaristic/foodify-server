package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.auth.application.PhoneNumberUtils;
import com.foodify.server.modules.auth.application.SmsSender;
import com.foodify.server.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.RefreshTokenRequest;
import com.foodify.server.modules.auth.dto.RegisterRequest;
import com.foodify.server.modules.auth.dto.phone.*;
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
import com.foodify.server.modules.identity.domain.PhoneSignupSession;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.PhoneSignupSessionRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Transactional
public class LocalIdentityAuthService implements IdentityAuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PhoneSignupSessionRepository phoneSignupSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SmsSender smsSender;

    private final SecureRandom random = new SecureRandom();

    private static final Duration CODE_VALIDITY = Duration.ofMinutes(5);
    private static final Duration RESEND_INTERVAL = Duration.ofSeconds(45);
    private static final int MAX_ATTEMPTS = 5;
    private static final int MAX_RESENDS = 3;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

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
            return buildGoogleResponse(user);
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

        return buildGoogleResponse(saved);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return buildLoginResponse(user);
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

        return buildLoginResponse(user);
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
                jwtService.generateAccessToken(saved),
                jwtService.generateRefreshToken(saved),
                jwtService.getTokenType(),
                jwtService.getAccessTtl().toSeconds(),
                jwtService.getScope()
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

            return new TokenRefreshResponse(
                    jwtService.generateAccessToken(user),
                    jwtService.getTokenType(),
                    jwtService.getAccessTtl().toSeconds(),
                    jwtService.getScope()
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", ex);
        }
    }

    @Override
    public PhoneSignupStateResponse startPhoneSignup(StartPhoneSignupRequest request) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());

        Client existingClient = clientRepository.findByPhoneNumber(phoneNumber).orElse(null);
        List<PhoneSignupSession> sessions = phoneSignupSessionRepository.findAllByPhoneNumberAndCompletedFalse(phoneNumber);
        if (!sessions.isEmpty()) {
            phoneSignupSessionRepository.deleteAll(sessions);
        }

        PhoneSignupSession session = new PhoneSignupSession();
        session.setPhoneNumber(phoneNumber);
        issueNewVerificationCode(session, true);
        phoneSignupSessionRepository.save(session);

        smsSender.sendVerificationCode(phoneNumber, session.getVerificationCode());

        return mapToState(session, existingClient != null);
    }

    @Override
    public PhoneSignupStateResponse resendPhoneVerification(ResendPhoneCodeRequest request) {
        PhoneSignupSession session = getSessionOrThrow(request.getSessionId());
        if (session.isPhoneVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already verified");
        }

        if (session.getResendCount() >= MAX_RESENDS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum resend attempts reached");
        }

        Instant now = Instant.now();
        Instant nextAllowed = session.getLastCodeSentAt().plus(RESEND_INTERVAL);
        if (now.isBefore(nextAllowed)) {
            long seconds = Duration.between(now, nextAllowed).toSeconds();
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Please wait " + seconds + " seconds before requesting a new code");
        }

        session.setResendCount(session.getResendCount() + 1);
        issueNewVerificationCode(session, true);
        phoneSignupSessionRepository.save(session);

        smsSender.sendVerificationCode(session.getPhoneNumber(), session.getVerificationCode());

        return mapToState(session, isLoginAttempt(session));
    }

    @Override
    public PhoneSignupStateResponse verifyPhoneCode(VerifyPhoneCodeRequest request) {
        PhoneSignupSession session = getSessionOrThrow(request.getSessionId());
        if (session.isPhoneVerified()) {
            return mapToState(session, isLoginAttempt(session));
        }

        if (session.getFailedAttemptCount() >= MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Maximum verification attempts exceeded");
        }

        Instant now = Instant.now();
        if (session.getCodeExpiresAt().isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }

        if (!session.getVerificationCode().equals(request.getCode())) {
            session.setFailedAttemptCount(session.getFailedAttemptCount() + 1);
            phoneSignupSessionRepository.save(session);
            int remaining = Math.max(0, MAX_ATTEMPTS - session.getFailedAttemptCount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code. Attempts remaining: " + remaining);
        }

        session.setPhoneVerifiedAt(now);
        session.setFailedAttemptCount(0);
        phoneSignupSessionRepository.save(session);

        Client existingClient = clientRepository.findByPhoneNumber(session.getPhoneNumber()).orElse(null);
        if (existingClient != null) {
            String accessToken = jwtService.generateAccessToken(existingClient);
            String refreshToken = jwtService.generateRefreshToken(existingClient);
            PhoneSignupStateResponse state = mapToState(session, true);
            return state.toBuilder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType(jwtService.getTokenType())
                    .expiresIn(jwtService.getAccessTtl().toSeconds())
                    .scope(jwtService.getScope())
                    .user(buildAuthenticatedClientResponse(existingClient))
                    .loginAttempt(true)
                    .build();
        }

        return mapToState(session, false);
    }

    @Override
    public PhoneSignupStateResponse captureSignupEmail(CaptureEmailRequest request) {
        PhoneSignupSession session = requirePhoneVerified(request.getSessionId());
        String email = normalizeEmail(request.getEmail());
        if (!StringUtils.hasText(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        session.setEmail(email);
        session.setEmailCapturedAt(Instant.now());
        phoneSignupSessionRepository.save(session);
        return mapToState(session, isLoginAttempt(session));
    }

    @Override
    public PhoneSignupStateResponse captureSignupName(CaptureNameRequest request) {
        PhoneSignupSession session = requireEmailCaptured(request.getSessionId());
        String firstName = sanitizeName(request.getFirstName());
        String lastName = sanitizeName(request.getLastName());

        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both first and last names are required");
        }

        session.setFirstName(firstName);
        session.setLastName(lastName);
        session.setNameCapturedAt(Instant.now());
        phoneSignupSessionRepository.save(session);
        return mapToState(session, isLoginAttempt(session));
    }

    @Override
    public CompletePhoneSignupResponse acceptSignupLegal(AcceptLegalRequest request) {
        if (!request.isAccepted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Terms must be accepted to continue");
        }
        PhoneSignupSession session = requireNameCaptured(request.getSessionId());

        if (session.isCompleted()) {
            Client existing = clientRepository.findByPhoneNumber(session.getPhoneNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Signup already completed"));
            return buildCompletionResponse(session, existing);
        }

        if (clientRepository.existsByPhoneNumber(session.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already registered");
        }
        if (session.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required before completion");
        }
        if (userRepository.existsByEmail(session.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        Client client = new Client();
        client.setPhoneNumber(session.getPhoneNumber());
        client.setPhoneVerified(true);
        client.setEmail(session.getEmail());
        client.setEmailVerified(false);
        client.setName(buildFullName(session));
        client.setRole(Role.CLIENT);
        client.setAuthProvider(AuthProvider.PHONE);

        Client saved = clientRepository.save(client);

        session.setTermsAcceptedAt(Instant.now());
        session.setCompleted(true);
        phoneSignupSessionRepository.save(session);

        return buildCompletionResponse(session, saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PhoneSignupStateResponse getSignupState(String sessionId) {
        PhoneSignupSession session = phoneSignupSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        return mapToState(session, isLoginAttempt(session));
    }

    private PhoneSignupSession getSessionOrThrow(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session ID is required");
        }
        return phoneSignupSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    private PhoneSignupSession requirePhoneVerified(String sessionId) {
        PhoneSignupSession session = getSessionOrThrow(sessionId);
        if (!session.isPhoneVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be verified first");
        }
        return session;
    }

    private PhoneSignupSession requireEmailCaptured(String sessionId) {
        PhoneSignupSession session = requirePhoneVerified(sessionId);
        if (!session.isEmailProvided()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required before continuing");
        }
        return session;
    }

    private PhoneSignupSession requireNameCaptured(String sessionId) {
        PhoneSignupSession session = requireEmailCaptured(sessionId);
        if (!session.isNameProvided()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must be provided before continuing");
        }
        return session;
    }

    private void issueNewVerificationCode(PhoneSignupSession session, boolean resetAttempts) {
        String code = String.format(Locale.US, "%06d", random.nextInt(1_000_000));
        Instant now = Instant.now();
        session.setVerificationCode(code);
        session.setCodeExpiresAt(now.plus(CODE_VALIDITY));
        session.setLastCodeSentAt(now);
        if (resetAttempts) {
            session.setFailedAttemptCount(0);
        }
    }

    private PhoneSignupStateResponse mapToState(PhoneSignupSession session, boolean loginAttempt) {
        String nextStep = determineNextStep(session);
        Instant resendAvailableAt = session.getLastCodeSentAt() == null
                ? null
                : session.getLastCodeSentAt().plus(RESEND_INTERVAL);
        Instant codeExpiresAt = session.isPhoneVerified() ? null : session.getCodeExpiresAt();
        Integer attemptsRemaining = session.isPhoneVerified() ? null : Math.max(0, MAX_ATTEMPTS - session.getFailedAttemptCount());
        Integer resendsRemaining = session.isPhoneVerified() ? 0 : Math.max(0, MAX_RESENDS - session.getResendCount());

        return PhoneSignupStateResponse.builder()
                .sessionId(session.getSessionId())
                .phoneNumber(session.getPhoneNumber())
                .phoneVerified(session.isPhoneVerified())
                .emailProvided(session.isEmailProvided())
                .nameProvided(session.isNameProvided())
                .termsAccepted(session.isTermsAccepted())
                .completed(session.isCompleted())
                .nextStep(nextStep)
                .codeExpiresAt(codeExpiresAt)
                .resendAvailableAt(resendAvailableAt)
                .attemptsRemaining(attemptsRemaining)
                .resendsRemaining(resendsRemaining)
                .email(session.getEmail())
                .firstName(session.getFirstName())
                .lastName(session.getLastName())
                .loginAttempt(loginAttempt)
                .build();
    }

    private boolean isLoginAttempt(PhoneSignupSession session) {
        return !session.isCompleted() && clientRepository.existsByPhoneNumber(session.getPhoneNumber());
    }

    private CompletePhoneSignupResponse buildCompletionResponse(PhoneSignupSession session, Client client) {
        PhoneSignupStateResponse state = mapToState(session, true);
        AuthenticatedClientResponse user = buildAuthenticatedClientResponse(client);

        return CompletePhoneSignupResponse.builder()
                .state(state)
                .accessToken(jwtService.generateAccessToken(client))
                .refreshToken(jwtService.generateRefreshToken(client))
                .tokenType(jwtService.getTokenType())
                .expiresIn(jwtService.getAccessTtl().toSeconds())
                .scope(jwtService.getScope())
                .user(user)
                .build();
    }

    private AuthenticatedClientResponse buildAuthenticatedClientResponse(Client client) {
        return AuthenticatedClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhoneNumber())
                .phoneVerified(client.getPhoneVerified())
                .emailVerified(client.getEmailVerified())
                .role(client.getRole())
                .build();
    }

    private String determineNextStep(PhoneSignupSession session) {
        if (!session.isPhoneVerified()) {
            return "VERIFY_PHONE_CODE";
        }
        if (!session.isEmailProvided()) {
            return "PROVIDE_EMAIL";
        }
        if (!session.isNameProvided()) {
            return "PROVIDE_NAME";
        }
        if (!session.isTermsAccepted()) {
            return "ACCEPT_LEGAL_TERMS";
        }
        return "COMPLETED";
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.US);
    }

    private String sanitizeName(String name) {
        return name == null ? null : name.trim();
    }

    private String buildFullName(PhoneSignupSession session) {
        return (session.getFirstName() + " " + session.getLastName()).trim();
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

    private LoginResponse buildLoginResponse(User user) {
        return new LoginResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                jwtService.getTokenType(),
                jwtService.getAccessTtl().toSeconds(),
                jwtService.getScope(),
                mapUser(user)
        );
    }

    private GoogleRegisterResponse buildGoogleResponse(Client client) {
        return new GoogleRegisterResponse(
                client.getEmail(),
                client.getName(),
                client.getGoogleId(),
                jwtService.generateAccessToken(client),
                jwtService.generateRefreshToken(client),
                jwtService.getTokenType(),
                jwtService.getAccessTtl().toSeconds(),
                jwtService.getScope()
        );
    }
}
