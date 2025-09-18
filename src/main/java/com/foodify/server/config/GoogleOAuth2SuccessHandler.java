package com.foodify.server.config;

import com.foodify.server.enums.Role;
import com.foodify.server.models.Client;
import com.foodify.server.models.User;
import com.foodify.server.repository.ClientRepository;
import com.foodify.server.repository.UserRepository;
import com.foodify.server.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private HttpSession session;

    @Autowired private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        String phone = (String) session.getAttribute("pendingPhone");

        Client client = clientRepository.findByEmail(email).orElse(null);

        if (phone == null && client == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing verified phone");
            return;
        }

        // Save client
        if (client == null) {
            client = new Client();
            client.setEmail(email);
            client.setName(name);
            client.setPhoneNumber(phone);
            client.setPhoneVerified(true);
            client.setEmailVerified(true);
            client.setGoogleId(googleId);
            client.setRole(Role.CLIENT);
            clientRepository.save(client);
        }

        String token = jwtService.generateToken(client);
        response.sendRedirect("http://localhost:8080/after-login?token=" + token);
    }
}
