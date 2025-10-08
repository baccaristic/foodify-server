package com.foodify.server.modules.identity.application;

import com.foodify.server.modules.auth.dto.LoginRequest;
import com.foodify.server.modules.auth.dto.phone.StartPhoneSignupRequest;
import com.foodify.server.modules.identity.api.LoginResponse;
import com.foodify.server.modules.auth.dto.phone.PhoneSignupStateResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class RemoteIdentityAuthServiceContractTest {

    @Test
    void loginPublishesOidcShape() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse()
                    .setHeader("Content-Type", "application/json")
                    .setBody("{" +
                            "\"accessToken\":\"access\"," +
                            "\"refreshToken\":\"refresh\"," +
                            "\"tokenType\":\"Bearer\"," +
                            "\"expiresIn\":3600," +
                            "\"scope\":\"openid profile email\"," +
                            "\"user\":{\"id\":1,\"email\":\"user@foodify.test\",\"name\":\"User\",\"role\":\"CLIENT\"}" +
                            "}"));

            RestClient client = RestClient.builder()
                    .baseUrl(server.url("/").toString())
                    .build();
            RemoteIdentityAuthService service = new RemoteIdentityAuthService(client);

            LoginRequest request = new LoginRequest();
            request.setEmail("user@foodify.test");
            request.setPassword("secret");

            LoginResponse response = service.login(request);

            assertThat(response.accessToken()).isEqualTo("access");
            assertThat(response.tokenType()).isEqualTo("Bearer");
            assertThat(response.expiresIn()).isEqualTo(3600);
            assertThat(response.scope()).isEqualTo("openid profile email");
            assertThat(response.user().email()).isEqualTo("user@foodify.test");

            RecordedRequest recorded = server.takeRequest();
            assertThat(recorded.getPath()).isEqualTo("/api/auth/login");
            assertThat(recorded.getBody().readUtf8()).contains("\"email\":\"user@foodify.test\"");
        }
    }

    @Test
    void startPhoneSignupContractsOnJsonShape() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse()
                    .setHeader("Content-Type", "application/json")
                    .setBody("{" +
                            "\"sessionId\":\"abc\"," +
                            "\"phoneNumber\":\"+15555550100\"," +
                            "\"phoneVerified\":false," +
                            "\"emailProvided\":false," +
                            "\"nameProvided\":false," +
                            "\"termsAccepted\":false," +
                            "\"completed\":false," +
                            "\"nextStep\":\"VERIFY_PHONE_CODE\"," +
                            "\"loginAttempt\":false" +
                            "}"));

            RestClient client = RestClient.builder()
                    .baseUrl(server.url("/").toString())
                    .build();
            RemoteIdentityAuthService service = new RemoteIdentityAuthService(client);

            StartPhoneSignupRequest request = new StartPhoneSignupRequest();
            request.setPhoneNumber("+1 555 555 0100");

            PhoneSignupStateResponse response = service.startPhoneSignup(request);

            assertThat(response.getPhoneNumber()).isEqualTo("+15555550100");
            assertThat(response.isLoginAttempt()).isFalse();
            assertThat(response.getNextStep()).isEqualTo("VERIFY_PHONE_CODE");

            RecordedRequest recorded = server.takeRequest();
            assertThat(recorded.getPath()).isEqualTo("/api/auth/phone/start");
            assertThat(recorded.getBody().readUtf8()).contains("phoneNumber");
        }
    }
}
