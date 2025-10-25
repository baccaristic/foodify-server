package com.foodify.server.modules.auth.security;

import com.foodify.server.config.InternalAccessProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class InternalSecretFilter extends OncePerRequestFilter {

    private static final String INTERNAL_PREFIX = "/internal/";
    private static final String SECRET_HEADER = "X-Internal-Secret";

    private final InternalAccessProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith(INTERNAL_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String expected = properties.getSharedSecret();
        if (!StringUtils.hasText(expected)) {
            response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Internal secret is not configured");
            return;
        }

        String provided = request.getHeader(SECRET_HEADER);
        if (!expected.equals(provided)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized internal request");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
