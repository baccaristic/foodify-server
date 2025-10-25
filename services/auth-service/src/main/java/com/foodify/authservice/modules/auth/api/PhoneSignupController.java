package com.foodify.authservice.modules.auth.api;

import com.foodify.authservice.modules.auth.application.PhoneSignupService;
import com.foodify.authservice.modules.auth.dto.phone.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/phone")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PhoneSignupController {
    private final PhoneSignupService phoneSignupService;

    @PostMapping("/start")
    public PhoneSignupStateResponse start(@RequestBody StartPhoneSignupRequest request) {
        return phoneSignupService.start(request);
    }

    @PostMapping("/verify")
    public PhoneSignupStateResponse verify(@RequestBody VerifyPhoneCodeRequest request) {
        return phoneSignupService.verify(request);
    }

    @PostMapping("/resend")
    public PhoneSignupStateResponse resend(@RequestBody ResendPhoneCodeRequest request) {
        return phoneSignupService.resend(request);
    }

    @PostMapping("/email")
    public PhoneSignupStateResponse captureEmail(@RequestBody CaptureEmailRequest request) {
        return phoneSignupService.captureEmail(request);
    }

    @PostMapping("/email/verify")
    public PhoneSignupStateResponse verifyEmail(@RequestBody VerifyEmailCodeRequest request) {
        return phoneSignupService.verifyEmail(request);
    }

    @PostMapping("/email/resend")
    public PhoneSignupStateResponse resendEmail(@RequestBody ResendEmailCodeRequest request) {
        return phoneSignupService.resendEmail(request);
    }

    @PostMapping("/name")
    public PhoneSignupStateResponse captureName(@RequestBody CaptureNameRequest request) {
        return phoneSignupService.captureName(request);
    }

    @PostMapping("/accept")
    public CompletePhoneSignupResponse acceptLegal(@RequestBody AcceptLegalRequest request) {
        return phoneSignupService.acceptLegal(request);
    }

    @GetMapping("/{sessionId}")
    public PhoneSignupStateResponse getState(@PathVariable String sessionId) {
        return phoneSignupService.getState(sessionId);
    }
}
