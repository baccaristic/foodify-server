package com.foodify.server.modules.auth.application;

import com.foodify.server.modules.auth.domain.PhoneSignupSession;
import com.foodify.server.modules.auth.dto.phone.*;
import com.foodify.server.modules.auth.repository.PhoneSignupSessionRepository;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class PhoneSignupService {
    private static final Duration CODE_VALIDITY = Duration.ofMinutes(5);
    private static final Duration RESEND_INTERVAL = Duration.ofSeconds(45);
    private static final int MAX_ATTEMPTS = 5;
    private static final int MAX_RESENDS = 3;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private final PhoneSignupSessionRepository sessionRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SmsSender smsSender;

    private final SecureRandom random = new SecureRandom();

    public PhoneSignupStateResponse start(StartPhoneSignupRequest request) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());

        Client existingClient = clientRepository.findByPhoneNumber(phoneNumber).orElse(null);

        List<PhoneSignupSession> existingSessions = sessionRepository.findAllByPhoneNumberAndCompletedFalse(phoneNumber);
        if (!existingSessions.isEmpty()) {
            sessionRepository.deleteAll(existingSessions);
        }

        PhoneSignupSession session = new PhoneSignupSession();
        session.setPhoneNumber(phoneNumber);
        issueNewVerificationCode(session, true);
        sessionRepository.save(session);

        smsSender.sendVerificationCode(phoneNumber, session.getVerificationCode());

        if (existingClient != null) {
            return mapToState(session, null, null, null, true);
        }

        return mapToState(session);
    }

    public PhoneSignupStateResponse resend(ResendPhoneCodeRequest request) {
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
        sessionRepository.save(session);

        smsSender.sendVerificationCode(session.getPhoneNumber(), session.getVerificationCode());

        return mapToState(session);
    }

    public PhoneSignupStateResponse verify(VerifyPhoneCodeRequest request) {
        PhoneSignupSession session = getSessionOrThrow(request.getSessionId());
        if (session.isPhoneVerified()) {
            return mapToState(session);
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
            sessionRepository.save(session);
            int remaining = Math.max(0, MAX_ATTEMPTS - session.getFailedAttemptCount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code. Attempts remaining: " + remaining);
        }

        session.setPhoneVerifiedAt(now);
        session.setFailedAttemptCount(0);
        sessionRepository.save(session);

        Client existingClient = clientRepository.findByPhoneNumber(session.getPhoneNumber()).orElse(null);
        if (existingClient != null) {
            String accessToken = jwtService.generateAccessToken(existingClient);
            String refreshToken = jwtService.generateRefreshToken(existingClient);
            AuthenticatedClientResponse user = buildAuthenticatedClientResponse(existingClient);
            return mapToState(session, user, accessToken, refreshToken, true);
        }

        return mapToState(session);
    }

    public PhoneSignupStateResponse captureEmail(CaptureEmailRequest request) {
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
        sessionRepository.save(session);
        return mapToState(session);
    }

    public PhoneSignupStateResponse captureName(CaptureNameRequest request) {
        PhoneSignupSession session = requireEmailCaptured(request.getSessionId());
        String firstName = sanitizeName(request.getFirstName());
        String lastName = sanitizeName(request.getLastName());

        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both first and last names are required");
        }

        session.setFirstName(firstName);
        session.setLastName(lastName);
        session.setNameCapturedAt(Instant.now());
        sessionRepository.save(session);
        return mapToState(session);
    }

    public CompletePhoneSignupResponse acceptLegal(AcceptLegalRequest request) {
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
        sessionRepository.save(session);

        return buildCompletionResponse(session, saved);
    }

    @Transactional(readOnly = true)
    public PhoneSignupStateResponse getState(String sessionId) {
        PhoneSignupSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        return mapToState(session);
    }

    private PhoneSignupSession getSessionOrThrow(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session ID is required");
        }
        return sessionRepository.findBySessionId(sessionId)
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

    private PhoneSignupStateResponse mapToState(PhoneSignupSession session) {
        return mapToState(session, null, null, null, isLoginAttempt(session));
    }

    private PhoneSignupStateResponse mapToState(PhoneSignupSession session,
                                               AuthenticatedClientResponse user,
                                               String accessToken,
                                               String refreshToken,
                                               boolean loginAttempt) {
        boolean completed = session.isCompleted();
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
                .completed(completed)
                .nextStep(nextStep)
                .codeExpiresAt(codeExpiresAt)
                .resendAvailableAt(resendAvailableAt)
                .attemptsRemaining(attemptsRemaining)
                .resendsRemaining(resendsRemaining)
                .email(session.getEmail())
                .firstName(session.getFirstName())
                .lastName(session.getLastName())
                .loginAttempt(loginAttempt)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    private boolean isLoginAttempt(PhoneSignupSession session) {
        return !session.isCompleted() && clientRepository.existsByPhoneNumber(session.getPhoneNumber());
    }

    private CompletePhoneSignupResponse buildCompletionResponse(PhoneSignupSession session, Client client) {
        String accessToken = jwtService.generateAccessToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);
        PhoneSignupStateResponse state = mapToState(session);

        AuthenticatedClientResponse user = buildAuthenticatedClientResponse(client);

        return CompletePhoneSignupResponse.builder()
                .state(state)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
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
}
