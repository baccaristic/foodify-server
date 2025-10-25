package com.foodify.authservice.modules.auth.application;

import com.foodify.authservice.modules.auth.domain.PhoneSignupSession;
import com.foodify.authservice.modules.auth.dto.phone.*;
import com.foodify.authservice.modules.auth.repository.PhoneSignupSessionRepository;
import com.foodify.authservice.modules.auth.security.JwtService;
import com.foodify.authservice.modules.identity.domain.AuthProvider;
import com.foodify.authservice.modules.identity.domain.Client;
import com.foodify.authservice.modules.identity.domain.Role;
import com.foodify.authservice.modules.identity.repository.ClientRepository;
import com.foodify.authservice.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    private static final Duration EMAIL_CODE_VALIDITY = Duration.ofMinutes(10);
    private static final Duration EMAIL_RESEND_INTERVAL = Duration.ofMinutes(1);
    private static final int EMAIL_MAX_ATTEMPTS = 5;
    private static final int EMAIL_MAX_RESENDS = 3;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private final PhoneSignupSessionRepository sessionRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SmsSender smsSender;
    private final EmailSender emailSender;

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

        Instant now = Instant.now();
        session.setEmail(email);
        session.setEmailCapturedAt(now);
        session.setEmailVerifiedAt(null);
        session.setEmailResendCount(0);
        session.setEmailFailedAttemptCount(0);
        issueNewEmailVerificationCode(session, true);
        sessionRepository.save(session);

        emailSender.sendVerificationCode(email, session.getEmailVerificationCode());
        return mapToState(session);
    }

    public PhoneSignupStateResponse verifyEmail(VerifyEmailCodeRequest request) {
        PhoneSignupSession session = requireEmailProvided(request.getSessionId());
        if (session.isEmailVerified()) {
            return mapToState(session);
        }
        if (!StringUtils.hasText(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code is required");
        }
        if (session.getEmailVerificationCode() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has not been issued");
        }
        if (session.getEmailFailedAttemptCount() >= EMAIL_MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Maximum verification attempts exceeded");
        }

        Instant now = Instant.now();
        if (session.getEmailCodeExpiresAt() == null || session.getEmailCodeExpiresAt().isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }

        if (!session.getEmailVerificationCode().equals(request.getCode())) {
            session.setEmailFailedAttemptCount(session.getEmailFailedAttemptCount() + 1);
            sessionRepository.save(session);
            int remaining = Math.max(0, EMAIL_MAX_ATTEMPTS - session.getEmailFailedAttemptCount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code. Attempts remaining: " + remaining);
        }

        session.setEmailVerifiedAt(now);
        session.setEmailFailedAttemptCount(0);
        sessionRepository.save(session);
        return mapToState(session);
    }

    public PhoneSignupStateResponse resendEmail(ResendEmailCodeRequest request) {
        PhoneSignupSession session = requireEmailProvided(request.getSessionId());
        if (session.isEmailVerified()) {
            return mapToState(session);
        }
        if (session.getEmailResendCount() >= EMAIL_MAX_RESENDS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum resend attempts reached");
        }
        Instant now = Instant.now();
        Instant lastSentAt = session.getEmailLastCodeSentAt();
        if (lastSentAt != null) {
            Instant nextAllowed = lastSentAt.plus(EMAIL_RESEND_INTERVAL);
            if (now.isBefore(nextAllowed)) {
                long seconds = Duration.between(now, nextAllowed).toSeconds();
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Please wait " + seconds + " seconds before requesting a new code");
            }
        }

        session.setEmailResendCount(session.getEmailResendCount() + 1);
        issueNewEmailVerificationCode(session, true);
        sessionRepository.save(session);

        emailSender.sendVerificationCode(session.getEmail(), session.getEmailVerificationCode());
        return mapToState(session);
    }

    public PhoneSignupStateResponse captureName(CaptureNameRequest request) {
        PhoneSignupSession session = requireEmailVerified(request.getSessionId());
        String firstName = sanitizeName(request.getFirstName());
        String lastName = sanitizeName(request.getLastName());
        LocalDate dateOfBirth = parseDateOfBirth(request.getDateOfBirth());

        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both first and last names are required");
        }

        session.setFirstName(firstName);
        session.setLastName(lastName);
        session.setDateOfBirth(dateOfBirth);
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
        client.setEmailVerified(true);
        client.setName(buildFullName(session));
        client.setDateOfBirth(session.getDateOfBirth());
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

    private PhoneSignupSession requireEmailProvided(String sessionId) {
        PhoneSignupSession session = requirePhoneVerified(sessionId);
        if (!session.isEmailProvided()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required before continuing");
        }
        return session;
    }

    private PhoneSignupSession requireEmailVerified(String sessionId) {
        PhoneSignupSession session = requireEmailProvided(sessionId);
        if (!session.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must be verified before continuing");
        }
        return session;
    }

    private PhoneSignupSession requireNameCaptured(String sessionId) {
        PhoneSignupSession session = requireEmailVerified(sessionId);
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

    private void issueNewEmailVerificationCode(PhoneSignupSession session, boolean resetAttempts) {
        String code = String.format(Locale.US, "%06d", random.nextInt(1_000_000));
        Instant now = Instant.now();
        session.setEmailVerificationCode(code);
        session.setEmailCodeExpiresAt(now.plus(EMAIL_CODE_VALIDITY));
        session.setEmailLastCodeSentAt(now);
        if (resetAttempts) {
            session.setEmailFailedAttemptCount(0);
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
        Instant emailResendAvailableAt = null;
        Instant emailCodeExpiresAt = null;
        Integer emailAttemptsRemaining = null;
        Integer emailResendsRemaining = null;
        if (session.isEmailProvided() && !session.isEmailVerified()) {
            emailResendAvailableAt = session.getEmailLastCodeSentAt() == null
                    ? null
                    : session.getEmailLastCodeSentAt().plus(EMAIL_RESEND_INTERVAL);
            emailCodeExpiresAt = session.getEmailCodeExpiresAt();
            emailAttemptsRemaining = Math.max(0, EMAIL_MAX_ATTEMPTS - session.getEmailFailedAttemptCount());
            emailResendsRemaining = Math.max(0, EMAIL_MAX_RESENDS - session.getEmailResendCount());
        }

        return PhoneSignupStateResponse.builder()
                .sessionId(session.getSessionId())
                .phoneNumber(session.getPhoneNumber())
                .phoneVerified(session.isPhoneVerified())
                .emailProvided(session.isEmailProvided())
                .emailVerified(session.isEmailVerified())
                .nameProvided(session.isNameProvided())
                .termsAccepted(session.isTermsAccepted())
                .completed(completed)
                .nextStep(nextStep)
                .codeExpiresAt(codeExpiresAt)
                .resendAvailableAt(resendAvailableAt)
                .attemptsRemaining(attemptsRemaining)
                .resendsRemaining(resendsRemaining)
                .emailCodeExpiresAt(emailCodeExpiresAt)
                .emailResendAvailableAt(emailResendAvailableAt)
                .emailAttemptsRemaining(emailAttemptsRemaining)
                .emailResendsRemaining(emailResendsRemaining)
                .email(session.getEmail())
                .firstName(session.getFirstName())
                .lastName(session.getLastName())
                .dateOfBirth(session.getDateOfBirth())
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

    private LocalDate parseDateOfBirth(String value) {
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

    private String determineNextStep(PhoneSignupSession session) {
        if (!session.isPhoneVerified()) {
            return "VERIFY_PHONE_CODE";
        }
        if (!session.isEmailProvided()) {
            return "PROVIDE_EMAIL";
        }
        if (!session.isEmailVerified()) {
            return "VERIFY_EMAIL_CODE";
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
                .dateOfBirth(client.getDateOfBirth())
                .role(client.getRole())
                .build();
    }
}
