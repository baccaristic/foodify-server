package com.foodify.server.modules.auth.application;

import com.foodify.server.modules.auth.dto.phone.*;
import com.foodify.server.modules.identity.application.IdentityAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneSignupService {
    private final IdentityAuthService identityAuthService;

    public PhoneSignupStateResponse start(StartPhoneSignupRequest request) {
        return identityAuthService.startPhoneSignup(request);
    }

    public PhoneSignupStateResponse resend(ResendPhoneCodeRequest request) {
        return identityAuthService.resendPhoneVerification(request);
    }

    public PhoneSignupStateResponse verify(VerifyPhoneCodeRequest request) {
        return identityAuthService.verifyPhoneCode(request);
    }

    public PhoneSignupStateResponse captureEmail(CaptureEmailRequest request) {
        return identityAuthService.captureSignupEmail(request);
    }

    public PhoneSignupStateResponse captureName(CaptureNameRequest request) {
        return identityAuthService.captureSignupName(request);
    }

    public CompletePhoneSignupResponse acceptLegal(AcceptLegalRequest request) {
        return identityAuthService.acceptSignupLegal(request);
    }

    public PhoneSignupStateResponse getState(String sessionId) {
        return identityAuthService.getSignupState(sessionId);
    }
}
