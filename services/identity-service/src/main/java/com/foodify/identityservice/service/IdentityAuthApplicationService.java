package com.foodify.identityservice.service;

import com.foodify.identityservice.domain.IdentityUser;
import com.foodify.identityservice.phone.PhoneSignupSession;
import com.foodify.identityservice.repository.IdentityUserRepository;
import com.foodify.identityservice.repository.PhoneSignupSessionRepository;
import com.foodify.server.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.RefreshTokenRequest;
import com.foodify.server.modules.auth.dto.RegisterRequest;
import com.foodify.server.modules.auth.dto.phone.*;
import com.foodify.server.modules.identity.api.*;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class IdentityAuthApplicationService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final Duration CODE_VALIDITY = Duration.ofMinutes(5);
    private static final Duration RESEND_INTERVAL = Duration.ofSeconds(45);
    private static final int MAX_ATTEMPTS = 5;
    private static final int MAX_RESENDS = 3;

    private final IdentityUserRepository identityUserRepository;
    private final PhoneSignupSessionRepository phoneSignupSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final OidcTokenService tokenService;
    private final SmsSender smsSender;

    public GoogleRegisterResponse registerWithGoogle(GoogleRegisterRequest request) {
        if (!StringUtils.hasText(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        String normalizedPhone = PhoneNumberUtils.normalize(request.getPhone());
        Optional<IdentityUser> existing = identityUserRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            IdentityUser user = existing.get();
            if (!StringUtils.hasText(user.getPhone())) {
                user.setPhone(normalizedPhone);
                user.setPhoneVerified(true);
                identityUserRepository.save(user);
            }
            return buildGoogleResponse(user);
        }
        if (identityUserRepository.existsByPhone(normalizedPhone)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already registered");
        }
        IdentityUser user = new IdentityUser();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setGoogleId(request.getGoogleId());
        user.setPhone(normalizedPhone);
        user.setPhoneVerified(true);
        user.setEmailVerified(true);
        user.setRole(Role.CLIENT);
        user.setAuthProvider(AuthProvider.GOOGLE);
        IdentityUser saved = identityUserRepository.save(user);
        return buildGoogleResponse(saved);
    }

    public LoginResponse login(LoginRequest request) {
        IdentityUser user = identityUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return buildLoginResponse(user);
    }

    public LoginResponse driverLogin(LoginRequest request) {
        IdentityUser user = identityUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (user.getRole() != Role.DRIVER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Driver role required");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return buildLoginResponse(user);
    }

    public CompleteRegistrationResponse completeRegistration(RegisterRequest request) {
        if (identityUserRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        IdentityUser user = new IdentityUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(Role.CLIENT);
        user.setAuthProvider(request.isGoogleAccount() ? AuthProvider.GOOGLE : AuthProvider.LOCAL);
        user.setPhoneVerified(request.isPhoneVerified());
        user.setEmailVerified(request.isEmailVerified());
        if (!request.isGoogleAccount()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        IdentityUser saved = identityUserRepository.save(user);
        return new CompleteRegistrationResponse(
                mapUser(saved),
                tokenService.generateAccessToken(saved),
                tokenService.generateRefreshToken(saved),
                tokenService.getTokenType(),
                tokenService.getAccessExpiresIn(),
                tokenService.getScope()
        );
    }

    public IdentityHeartbeatResponse heartbeat(String bearerToken) {
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No token");
        }
        return new IdentityHeartbeatResponse("active", null);
    }

    public TokenRefreshResponse refresh(RefreshTokenRequest request) {
        if (request == null || !StringUtils.hasText(request.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        }
        try {
            Claims claims = tokenService.parseRefreshToken(request.getRefreshToken());
            if (tokenService.isRefreshTokenExpired(request.getRefreshToken())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
            }
            Long userId = Long.valueOf(claims.getSubject());
            IdentityUser user = identityUserRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
            return new TokenRefreshResponse(
                    tokenService.generateAccessToken(user),
                    tokenService.getTokenType(),
                    tokenService.getAccessExpiresIn(),
                    tokenService.getScope()
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", ex);
        }
    }

    public PhoneSignupStateResponse startPhoneSignup(StartPhoneSignupRequest request) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());
        IdentityUser existingClient = identityUserRepository.findByPhone(phoneNumber).orElse(null);
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
        IdentityUser existingClient = identityUserRepository.findByPhone(session.getPhoneNumber()).orElse(null);
        if (existingClient != null) {
            PhoneSignupStateResponse state = mapToState(session, true);
            return state.toBuilder()
                    .accessToken(tokenService.generateAccessToken(existingClient))
                    .refreshToken(tokenService.generateRefreshToken(existingClient))
                    .tokenType(tokenService.getTokenType())
                    .expiresIn(tokenService.getAccessExpiresIn())
                    .scope(tokenService.getScope())
                    .user(buildAuthenticatedClientResponse(existingClient))
                    .loginAttempt(true)
                    .build();
        }
        return mapToState(session, false);
    }

    public PhoneSignupStateResponse captureSignupEmail(CaptureEmailRequest request) {
        PhoneSignupSession session = requirePhoneVerified(request.getSessionId());
        String email = normalizeEmail(request.getEmail());
        if (!StringUtils.hasText(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        if (identityUserRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        session.setEmail(email);
        session.setEmailCapturedAt(Instant.now());
        phoneSignupSessionRepository.save(session);
        return mapToState(session, isLoginAttempt(session));
    }

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

    public CompletePhoneSignupResponse acceptSignupLegal(AcceptLegalRequest request) {
        if (!request.isAccepted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Terms must be accepted to continue");
        }
        PhoneSignupSession session = requireNameCaptured(request.getSessionId());
        if (session.isCompleted()) {
            IdentityUser existing = identityUserRepository.findByPhone(session.getPhoneNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Signup already completed"));
            return buildCompletionResponse(session, existing);
        }
        if (identityUserRepository.existsByPhone(session.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already registered");
        }
        if (session.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required before completion");
        }
        if (identityUserRepository.existsByEmail(session.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        IdentityUser user = new IdentityUser();
        user.setPhone(session.getPhoneNumber());
        user.setPhoneVerified(true);
        user.setEmail(session.getEmail());
        user.setEmailVerified(false);
        user.setName(buildFullName(session));
        user.setRole(Role.CLIENT);
        user.setAuthProvider(AuthProvider.PHONE);
        IdentityUser saved = identityUserRepository.save(user);
        session.setTermsAcceptedAt(Instant.now());
        session.setCompleted(true);
        phoneSignupSessionRepository.save(session);
        return buildCompletionResponse(session, saved);
    }

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
        String code = String.format(Locale.US, "%06d", (int) (Math.random() * 1_000_000));
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
        Instant resendAvailableAt = session.getLastCodeSentAt() == null ? null : session.getLastCodeSentAt().plus(RESEND_INTERVAL);
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
        return !session.isCompleted() && identityUserRepository.existsByPhone(session.getPhoneNumber());
    }

    private CompletePhoneSignupResponse buildCompletionResponse(PhoneSignupSession session, IdentityUser user) {
        PhoneSignupStateResponse state = mapToState(session, true);
        AuthenticatedClientResponse client = buildAuthenticatedClientResponse(user);
        return CompletePhoneSignupResponse.builder()
                .state(state)
                .accessToken(tokenService.generateAccessToken(user))
                .refreshToken(tokenService.generateRefreshToken(user))
                .tokenType(tokenService.getTokenType())
                .expiresIn(tokenService.getAccessExpiresIn())
                .scope(tokenService.getScope())
                .user(client)
                .build();
    }

    private AuthenticatedClientResponse buildAuthenticatedClientResponse(IdentityUser user) {
        return AuthenticatedClientResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .phoneVerified(user.getPhoneVerified())
                .emailVerified(user.getEmailVerified())
                .role(user.getRole())
                .build();
    }

    private IdentityUserProfile mapUser(IdentityUser user) {
        return new IdentityUserProfile(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getPhone(),
                user.getAddress(),
                user.getEmailVerified(),
                user.getPhoneVerified(),
                null,
                user.getGoogleId()
        );
    }

    private LoginResponse buildLoginResponse(IdentityUser user) {
        return new LoginResponse(
                tokenService.generateAccessToken(user),
                tokenService.generateRefreshToken(user),
                tokenService.getTokenType(),
                tokenService.getAccessExpiresIn(),
                tokenService.getScope(),
                mapUser(user)
        );
    }

    private GoogleRegisterResponse buildGoogleResponse(IdentityUser user) {
        return new GoogleRegisterResponse(
                user.getEmail(),
                user.getName(),
                user.getGoogleId(),
                tokenService.generateAccessToken(user),
                tokenService.generateRefreshToken(user),
                tokenService.getTokenType(),
                tokenService.getAccessExpiresIn(),
                tokenService.getScope()
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.US);
    }

    private String sanitizeName(String name) {
        return name == null ? null : name.trim();
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

    private String buildFullName(PhoneSignupSession session) {
        return (session.getFirstName() + " " + session.getLastName()).trim();
    }
}
