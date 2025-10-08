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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

public class RemoteIdentityAuthService implements IdentityAuthService {

    private final RestClient identityRestClient;

    public RemoteIdentityAuthService(RestClient identityRestClient) {
        this.identityRestClient = identityRestClient;
    }

    @Override
    public GoogleRegisterResponse registerWithGoogle(GoogleRegisterRequest request) {
        return exchange(() -> identityRestClient.post()
                .uri("/api/auth/google")
                .body(request)
                .retrieve()
                .body(GoogleRegisterResponse.class));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return exchange(() -> identityRestClient.post()
                .uri("/api/auth/login")
                .body(request)
                .retrieve()
                .body(LoginResponse.class));
    }

    @Override
    public LoginResponse driverLogin(LoginRequest request) {
        return exchange(() -> identityRestClient.post()
                .uri("/api/auth/driver/login")
                .body(request)
                .retrieve()
                .body(LoginResponse.class));
    }

    @Override
    public CompleteRegistrationResponse completeRegistration(RegisterRequest request) {
        return exchange(() -> identityRestClient.post()
                .uri("/api/auth/register/complete")
                .body(request)
                .retrieve()
                .body(CompleteRegistrationResponse.class));
    }

    @Override
    public IdentityHeartbeatResponse heartbeat(String bearerToken) {
        return exchange(() -> identityRestClient.get()
                .uri("/api/auth/heart-beat")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .body(IdentityHeartbeatResponse.class));
    }

    @Override
    public TokenRefreshResponse refresh(RefreshTokenRequest request) {
        return exchange(() -> identityRestClient.post()
                .uri("/api/auth/refresh")
                .body(request)
                .retrieve()
                .body(TokenRefreshResponse.class));
    }

    private <T> T exchange(Supplier<T> supplier) {
        try {
            T response = supplier.get();
            if (response == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Identity service returned no content");
            }
            return response;
        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Identity service unavailable", ex);
        }
    }
}
