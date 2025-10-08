package com.foodify.server.modules.auth.api;

import com.foodify.server.modules.auth.application.PhoneSignupService;
import com.foodify.server.modules.auth.dto.phone.*;
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
