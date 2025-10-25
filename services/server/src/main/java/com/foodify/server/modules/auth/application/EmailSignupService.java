package com.foodify.server.modules.auth.application;

import com.foodify.server.modules.auth.domain.EmailSignupSession;
import com.foodify.server.modules.auth.dto.email.*;
import com.foodify.server.modules.auth.dto.phone.AuthenticatedClientResponse;
import com.foodify.server.modules.auth.repository.EmailSignupSessionRepository;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.domain.AuthProvider;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.domain.Role;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class EmailSignupService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final Duration EMAIL_CODE_VALIDITY = Duration.ofMinutes(10);
    private static final Duration EMAIL_RESEND_INTERVAL = Duration.ofMinutes(1);
    private static final int EMAIL_MAX_ATTEMPTS = 5;
    private static final int EMAIL_MAX_RESENDS = 3;
    private static final Duration PHONE_CODE_VALIDITY = Duration.ofMinutes(5);
    private static final Duration PHONE_RESEND_INTERVAL = Duration.ofSeconds(45);
    private static final int PHONE_MAX_ATTEMPTS = 5;
    private static final int PHONE_MAX_RESENDS = 3;

    private final EmailSignupSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final SmsSender smsSender;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom random = new SecureRandom();

    public EmailSignupStateResponse start(StartEmailSignupRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (!StringUtils.hasText(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }

        List<EmailSignupSession> existingSessions = sessionRepository.findAllByEmailAndCompletedFalse(email);
        if (!existingSessions.isEmpty()) {
            sessionRepository.deleteAll(existingSessions);
        }

        EmailSignupSession session = new EmailSignupSession();
        session.setEmail(email);
        session.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
        issueNewEmailVerificationCode(session, true);
        sessionRepository.save(session);

        emailSender.sendVerificationCode(email, session.getEmailVerificationCode());
        return mapToState(session);
    }

    public EmailSignupStateResponse resendEmailCode(ResendEmailCodeRequest request) {
        EmailSignupSession session = getSessionOrThrow(request.getSessionId());
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

    public EmailSignupStateResponse verifyEmail(VerifyEmailCodeRequest request) {
        EmailSignupSession session = getSessionOrThrow(request.getSessionId());
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid verification code. Attempts remaining: " + remaining);
        }

        session.setEmailVerifiedAt(now);
        session.setEmailFailedAttemptCount(0);
        sessionRepository.save(session);
        return mapToState(session);
    }

    public EmailSignupStateResponse capturePhone(CapturePhoneRequest request) {
        EmailSignupSession session = requireEmailVerified(request.getSessionId());
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());
        if (!StringUtils.hasText(phoneNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");
        }
        if (clientRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already registered");
        }

        session.setPhoneNumber(phoneNumber);
        session.setPhoneVerifiedAt(null);
        session.setPhoneResendCount(0);
        session.setPhoneFailedAttemptCount(0);
        issueNewPhoneVerificationCode(session, true);
        sessionRepository.save(session);

        smsSender.sendVerificationCode(phoneNumber, session.getPhoneVerificationCode());
        return mapToState(session);
    }

    public EmailSignupStateResponse resendPhoneCode(ResendPhoneCodeRequest request) {
        EmailSignupSession session = requirePhoneProvided(request.getSessionId());
        if (session.isPhoneVerified()) {
            return mapToState(session);
        }
        if (session.getPhoneResendCount() >= PHONE_MAX_RESENDS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Maximum resend attempts reached");
        }
        Instant now = Instant.now();
        Instant lastSentAt = session.getPhoneLastCodeSentAt();
        if (lastSentAt != null) {
            Instant nextAllowed = lastSentAt.plus(PHONE_RESEND_INTERVAL);
            if (now.isBefore(nextAllowed)) {
                long seconds = Duration.between(now, nextAllowed).toSeconds();
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Please wait " + seconds + " seconds before requesting a new code");
            }
        }

        session.setPhoneResendCount(session.getPhoneResendCount() + 1);
        issueNewPhoneVerificationCode(session, true);
        sessionRepository.save(session);

        smsSender.sendVerificationCode(session.getPhoneNumber(), session.getPhoneVerificationCode());
        return mapToState(session);
    }

    public EmailSignupStateResponse verifyPhone(VerifyPhoneCodeRequest request) {
        EmailSignupSession session = requirePhoneProvided(request.getSessionId());
        if (session.isPhoneVerified()) {
            return mapToState(session);
        }
        if (!StringUtils.hasText(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code is required");
        }
        if (session.getPhoneVerificationCode() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has not been issued");
        }
        if (session.getPhoneFailedAttemptCount() >= PHONE_MAX_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Maximum verification attempts exceeded");
        }
        Instant now = Instant.now();
        if (session.getPhoneCodeExpiresAt() == null || session.getPhoneCodeExpiresAt().isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }
        if (!session.getPhoneVerificationCode().equals(request.getCode())) {
            session.setPhoneFailedAttemptCount(session.getPhoneFailedAttemptCount() + 1);
            sessionRepository.save(session);
            int remaining = Math.max(0, PHONE_MAX_ATTEMPTS - session.getPhoneFailedAttemptCount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid verification code. Attempts remaining: " + remaining);
        }

        session.setPhoneVerifiedAt(now);
        session.setPhoneFailedAttemptCount(0);
        sessionRepository.save(session);
        return mapToState(session);
    }

    public EmailSignupStateResponse captureProfile(CaptureProfileRequest request) {
        EmailSignupSession session = requirePhoneVerified(request.getSessionId());
        String firstName = sanitizeName(request.getFirstName());
        String lastName = sanitizeName(request.getLastName());
        LocalDate dateOfBirth = parseDateOfBirth(request.getDateOfBirth());

        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both first and last names are required");
        }

        session.setFirstName(firstName);
        session.setLastName(lastName);
        session.setDateOfBirth(dateOfBirth);
        session.setProfileCapturedAt(Instant.now());
        sessionRepository.save(session);
        return mapToState(session);
    }

    public CompleteEmailSignupResponse acceptLegal(AcceptLegalRequest request) {
        if (!request.isAccepted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Terms must be accepted to continue");
        }
        EmailSignupSession session = requireProfileCaptured(request.getSessionId());

        if (session.isCompleted()) {
            Client existing = clientRepository.findByEmail(session.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Signup already completed"));
            return buildCompletionResponse(session, existing);
        }

        if (!session.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must be verified before completion");
        }
        if (!session.isPhoneVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be verified before completion");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(session.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        if (clientRepository.existsByPhoneNumber(session.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already registered");
        }

        Client client = new Client();
        client.setEmail(session.getEmail());
        client.setEmailVerified(true);
        client.setPhoneNumber(session.getPhoneNumber());
        client.setPhoneVerified(true);
        client.setName(buildFullName(session));
        client.setDateOfBirth(session.getDateOfBirth());
        client.setRole(Role.CLIENT);
        client.setAuthProvider(AuthProvider.LOCAL);
        client.setPassword(session.getEncodedPassword());

        Client saved = clientRepository.save(client);

        session.setTermsAcceptedAt(Instant.now());
        session.setCompleted(true);
        sessionRepository.save(session);

        return buildCompletionResponse(session, saved);
    }

    @Transactional(readOnly = true)
    public EmailSignupStateResponse getState(String sessionId) {
        EmailSignupSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        return mapToState(session);
    }

    private EmailSignupSession getSessionOrThrow(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session ID is required");
        }
        return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    private EmailSignupSession requireEmailVerified(String sessionId) {
        EmailSignupSession session = getSessionOrThrow(sessionId);
        if (!session.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email must be verified before continuing");
        }
        return session;
    }

    private EmailSignupSession requirePhoneProvided(String sessionId) {
        EmailSignupSession session = requireEmailVerified(sessionId);
        if (!session.isPhoneProvided()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required before continuing");
        }
        return session;
    }

    private EmailSignupSession requirePhoneVerified(String sessionId) {
        EmailSignupSession session = requirePhoneProvided(sessionId);
        if (!session.isPhoneVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be verified before continuing");
        }
        return session;
    }

    private EmailSignupSession requireProfileCaptured(String sessionId) {
        EmailSignupSession session = requirePhoneVerified(sessionId);
        if (!session.isProfileProvided()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile information must be provided before continuing");
        }
        return session;
    }

    private void issueNewEmailVerificationCode(EmailSignupSession session, boolean resetAttempts) {
        String code = String.format(Locale.US, "%06d", random.nextInt(1_000_000));
        Instant now = Instant.now();
        session.setEmailVerificationCode(code);
        session.setEmailCodeExpiresAt(now.plus(EMAIL_CODE_VALIDITY));
        session.setEmailLastCodeSentAt(now);
        if (resetAttempts) {
            session.setEmailFailedAttemptCount(0);
        }
    }

    private void issueNewPhoneVerificationCode(EmailSignupSession session, boolean resetAttempts) {
        String code = String.format(Locale.US, "%06d", random.nextInt(1_000_000));
        Instant now = Instant.now();
        session.setPhoneVerificationCode(code);
        session.setPhoneCodeExpiresAt(now.plus(PHONE_CODE_VALIDITY));
        session.setPhoneLastCodeSentAt(now);
        if (resetAttempts) {
            session.setPhoneFailedAttemptCount(0);
        }
    }

    private EmailSignupStateResponse mapToState(EmailSignupSession session) {
        return mapToState(session, null, null, null);
    }

    private EmailSignupStateResponse mapToState(EmailSignupSession session,
                                                AuthenticatedClientResponse user,
                                                String accessToken,
                                                String refreshToken) {
        boolean completed = session.isCompleted();
        String nextStep = determineNextStep(session);

        Instant emailResendAvailableAt = session.isEmailVerified() ? null
                : session.getEmailLastCodeSentAt() == null ? null
                : session.getEmailLastCodeSentAt().plus(EMAIL_RESEND_INTERVAL);
        Instant emailCodeExpiresAt = session.isEmailVerified() ? null : session.getEmailCodeExpiresAt();
        Integer emailAttemptsRemaining = session.isEmailVerified() ? null
                : Math.max(0, EMAIL_MAX_ATTEMPTS - session.getEmailFailedAttemptCount());
        Integer emailResendsRemaining = session.isEmailVerified() ? 0
                : Math.max(0, EMAIL_MAX_RESENDS - session.getEmailResendCount());

        Instant phoneResendAvailableAt = (!session.isPhoneProvided() || session.isPhoneVerified()) ? null
                : session.getPhoneLastCodeSentAt() == null ? null
                : session.getPhoneLastCodeSentAt().plus(PHONE_RESEND_INTERVAL);
        Instant phoneCodeExpiresAt = session.isPhoneVerified() ? null : session.getPhoneCodeExpiresAt();
        Integer phoneAttemptsRemaining = session.isPhoneVerified() ? null
                : Math.max(0, PHONE_MAX_ATTEMPTS - session.getPhoneFailedAttemptCount());
        Integer phoneResendsRemaining = session.isPhoneVerified() ? 0
                : Math.max(0, PHONE_MAX_RESENDS - session.getPhoneResendCount());

        return EmailSignupStateResponse.builder()
                .sessionId(session.getSessionId())
                .email(session.getEmail())
                .emailVerified(session.isEmailVerified())
                .phoneProvided(session.isPhoneProvided())
                .phoneVerified(session.isPhoneVerified())
                .profileProvided(session.isProfileProvided())
                .termsAccepted(session.isTermsAccepted())
                .completed(completed)
                .nextStep(nextStep)
                .emailCodeExpiresAt(emailCodeExpiresAt)
                .emailResendAvailableAt(emailResendAvailableAt)
                .emailAttemptsRemaining(emailAttemptsRemaining)
                .emailResendsRemaining(emailResendsRemaining)
                .phoneCodeExpiresAt(phoneCodeExpiresAt)
                .phoneResendAvailableAt(phoneResendAvailableAt)
                .phoneAttemptsRemaining(phoneAttemptsRemaining)
                .phoneResendsRemaining(phoneResendsRemaining)
                .phoneNumber(session.getPhoneNumber())
                .firstName(session.getFirstName())
                .lastName(session.getLastName())
                .dateOfBirth(session.getDateOfBirth())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
    }

    private String determineNextStep(EmailSignupSession session) {
        if (!session.isEmailVerified()) {
            return "VERIFY_EMAIL_CODE";
        }
        if (!session.isPhoneProvided()) {
            return "PROVIDE_PHONE";
        }
        if (!session.isPhoneVerified()) {
            return "VERIFY_PHONE_CODE";
        }
        if (!session.isProfileProvided()) {
            return "PROVIDE_PROFILE";
        }
        if (!session.isTermsAccepted()) {
            return "ACCEPT_LEGAL_TERMS";
        }
        return "COMPLETED";
    }

    private CompleteEmailSignupResponse buildCompletionResponse(EmailSignupSession session, Client client) {
        String accessToken = jwtService.generateAccessToken(client);
        String refreshToken = jwtService.generateRefreshToken(client);
        AuthenticatedClientResponse user = buildAuthenticatedClientResponse(client);
        EmailSignupStateResponse state = mapToState(session, user, accessToken, refreshToken);

        return CompleteEmailSignupResponse.builder()
                .state(state)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
                .dateOfBirth(client.getDateOfBirth())
                .role(client.getRole())
                .build();
    }

    private String buildFullName(EmailSignupSession session) {
        return (session.getFirstName() + " " + session.getLastName()).trim();
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
}
