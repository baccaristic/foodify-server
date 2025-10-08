package com.foodify.identityservice.controller;

import com.foodify.identityservice.service.IdentityAuthApplicationService;
import com.foodify.server.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.RefreshTokenRequest;
import com.foodify.server.modules.auth.dto.RegisterRequest;
import com.foodify.server.modules.auth.dto.phone.*;
import com.foodify.server.modules.identity.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class IdentityAuthController {
    private final IdentityAuthApplicationService service;

    @PostMapping("/google")
    public GoogleRegisterResponse registerWithGoogle(@RequestBody GoogleRegisterRequest request) {
        return service.registerWithGoogle(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/driver/login")
    public LoginResponse driverLogin(@RequestBody LoginRequest request) {
        return service.driverLogin(request);
    }

    @PostMapping("/register/complete")
    public CompleteRegistrationResponse completeRegistration(@RequestBody RegisterRequest request) {
        return service.completeRegistration(request);
    }

    @GetMapping("/heart-beat")
    public IdentityHeartbeatResponse heartbeat(@RequestHeader("Authorization") String authHeader) {
        return service.heartbeat(authHeader);
    }

    @PostMapping("/refresh")
    public TokenRefreshResponse refresh(@RequestBody RefreshTokenRequest request) {
        return service.refresh(request);
    }

    @PostMapping("/phone/start")
    public PhoneSignupStateResponse startPhone(@RequestBody StartPhoneSignupRequest request) {
        return service.startPhoneSignup(request);
    }

    @PostMapping("/phone/resend")
    public PhoneSignupStateResponse resend(@RequestBody ResendPhoneCodeRequest request) {
        return service.resendPhoneVerification(request);
    }

    @PostMapping("/phone/verify")
    public PhoneSignupStateResponse verify(@RequestBody VerifyPhoneCodeRequest request) {
        return service.verifyPhoneCode(request);
    }

    @PostMapping("/phone/email")
    public PhoneSignupStateResponse captureEmail(@RequestBody CaptureEmailRequest request) {
        return service.captureSignupEmail(request);
    }

    @PostMapping("/phone/name")
    public PhoneSignupStateResponse captureName(@RequestBody CaptureNameRequest request) {
        return service.captureSignupName(request);
    }

    @PostMapping("/phone/accept")
    public CompletePhoneSignupResponse acceptLegal(@RequestBody AcceptLegalRequest request) {
        return service.acceptSignupLegal(request);
    }

    @GetMapping("/phone/state/{sessionId}")
    public PhoneSignupStateResponse getState(@PathVariable String sessionId) {
        return service.getSignupState(sessionId);
    }
}
