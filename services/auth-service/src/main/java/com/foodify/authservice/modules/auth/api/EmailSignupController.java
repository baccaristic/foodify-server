package com.foodify.authservice.modules.auth.api;

import com.foodify.authservice.modules.auth.application.EmailSignupService;
import com.foodify.authservice.modules.auth.dto.email.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/email")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmailSignupController {
    private final EmailSignupService emailSignupService;

    @PostMapping("/start")
    public EmailSignupStateResponse start(@RequestBody StartEmailSignupRequest request) {
        return emailSignupService.start(request);
    }

    @PostMapping("/verify")
    public EmailSignupStateResponse verifyEmail(@RequestBody VerifyEmailCodeRequest request) {
        return emailSignupService.verifyEmail(request);
    }

    @PostMapping("/resend")
    public EmailSignupStateResponse resendEmail(@RequestBody ResendEmailCodeRequest request) {
        return emailSignupService.resendEmailCode(request);
    }

    @PostMapping("/phone")
    public EmailSignupStateResponse capturePhone(@RequestBody CapturePhoneRequest request) {
        return emailSignupService.capturePhone(request);
    }

    @PostMapping("/phone/verify")
    public EmailSignupStateResponse verifyPhone(@RequestBody VerifyPhoneCodeRequest request) {
        return emailSignupService.verifyPhone(request);
    }

    @PostMapping("/phone/resend")
    public EmailSignupStateResponse resendPhone(@RequestBody ResendPhoneCodeRequest request) {
        return emailSignupService.resendPhoneCode(request);
    }

    @PostMapping("/profile")
    public EmailSignupStateResponse captureProfile(@RequestBody CaptureProfileRequest request) {
        return emailSignupService.captureProfile(request);
    }

    @PostMapping("/accept")
    public CompleteEmailSignupResponse acceptLegal(@RequestBody AcceptLegalRequest request) {
        return emailSignupService.acceptLegal(request);
    }

    @GetMapping("/{sessionId}")
    public EmailSignupStateResponse getState(@PathVariable String sessionId) {
        return emailSignupService.getState(sessionId);
    }
}
