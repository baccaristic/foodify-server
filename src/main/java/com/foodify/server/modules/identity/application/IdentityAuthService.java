package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.auth.dto.GoogleRegisterRequest;
import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.RefreshTokenRequest;
import com.foodify.server.modules.auth.dto.RegisterRequest;
import com.foodify.server.modules.identity.api.CompleteRegistrationResponse;
import com.foodify.server.modules.identity.api.GoogleRegisterResponse;
import com.foodify.server.modules.identity.api.LoginResponse;
import com.foodify.server.modules.identity.api.IdentityHeartbeatResponse;
import com.foodify.server.modules.identity.api.TokenRefreshResponse;

public interface IdentityAuthService {

    GoogleRegisterResponse registerWithGoogle(GoogleRegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse driverLogin(LoginRequest request);

    CompleteRegistrationResponse completeRegistration(RegisterRequest request);

    IdentityHeartbeatResponse heartbeat(String bearerToken);

    TokenRefreshResponse refresh(RefreshTokenRequest request);
}
