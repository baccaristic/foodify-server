package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.RefreshTokenRequest;
import com.foodify.server.modules.auth.dto.RegisterRequest;
import com.foodify.server.modules.auth.dto.phone.AcceptLegalRequest;
import com.foodify.server.modules.auth.dto.phone.CaptureEmailRequest;
import com.foodify.server.modules.auth.dto.phone.CaptureNameRequest;
import com.foodify.server.modules.auth.dto.phone.PhoneSignupStateResponse;
import com.foodify.server.modules.auth.dto.phone.ResendPhoneCodeRequest;
import com.foodify.server.modules.auth.dto.phone.StartPhoneSignupRequest;
import com.foodify.server.modules.auth.dto.phone.VerifyPhoneCodeRequest;
import com.foodify.server.modules.identity.api.CompleteRegistrationResponse;
import com.foodify.server.modules.identity.api.GoogleRegisterResponse;
import com.foodify.server.modules.identity.api.LoginResponse;
import com.foodify.server.modules.identity.api.IdentityHeartbeatResponse;
import com.foodify.server.modules.identity.api.TokenRefreshResponse;
import com.foodify.server.modules.auth.dto.phone.CompletePhoneSignupResponse;

public interface IdentityAuthService {

    GoogleRegisterResponse registerWithGoogle(GoogleRegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse driverLogin(LoginRequest request);

    CompleteRegistrationResponse completeRegistration(RegisterRequest request);

    IdentityHeartbeatResponse heartbeat(String bearerToken);

    TokenRefreshResponse refresh(RefreshTokenRequest request);

    PhoneSignupStateResponse startPhoneSignup(StartPhoneSignupRequest request);

    PhoneSignupStateResponse resendPhoneVerification(ResendPhoneCodeRequest request);

    PhoneSignupStateResponse verifyPhoneCode(VerifyPhoneCodeRequest request);

    PhoneSignupStateResponse captureSignupEmail(CaptureEmailRequest request);

    PhoneSignupStateResponse captureSignupName(CaptureNameRequest request);

    CompletePhoneSignupResponse acceptSignupLegal(AcceptLegalRequest request);

    PhoneSignupStateResponse getSignupState(String sessionId);
}
