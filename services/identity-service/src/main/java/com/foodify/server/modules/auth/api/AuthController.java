package com.foodify.server.modules.auth.api;

import com.foodify.server.modules.auth.dto.*;
import com.foodify.server.modules.identity.api.CompleteRegistrationResponse;
import com.foodify.server.modules.identity.api.GoogleRegisterResponse;
import com.foodify.server.modules.identity.api.IdentityHeartbeatResponse;
import com.foodify.server.modules.identity.api.LoginResponse;
import com.foodify.server.modules.identity.api.TokenRefreshResponse;
import com.foodify.server.modules.identity.application.IdentityAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final IdentityAuthService identityAuthService;

    @PostMapping("/google")
    public ResponseEntity<GoogleRegisterResponse> registerWithGoogle(@RequestBody GoogleRegisterRequest request) {
        return ResponseEntity.ok(identityAuthService.registerWithGoogle(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(identityAuthService.login(request));
    }

    @PostMapping("/driver/login")
    public ResponseEntity<LoginResponse> driverLogin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(identityAuthService.driverLogin(request));
    }


    @PostMapping("/register/complete")
    public ResponseEntity<CompleteRegistrationResponse> completeRegistration(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(identityAuthService.completeRegistration(request));
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("uploads").resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/heart-beat")
    public ResponseEntity<IdentityHeartbeatResponse> checkSession(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(identityAuthService.heartbeat(authHeader));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(identityAuthService.refresh(request));
    }

}
