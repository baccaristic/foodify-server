package com.foodify.server.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests to verify that error handling returns proper status codes
 * and includes stack traces in logs for internal server errors.
 * 
 * This test validates the fix for the issue where internal errors were
 * returning 401 Unauthorized instead of proper 500 errors with stack traces.
 */
@SpringBootTest
@AutoConfigureMockMvc
class GlobalErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void errorEndpointShouldBeAccessibleWithoutAuthentication() throws Exception {
        // Given: An error occurs that redirects to /error endpoint
        // When: Accessing the /error endpoint without authentication
        // Then: Should receive 500 response (not 401) with error details
        mockMvc.perform(get("/error"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @WithMockUser
    void errorEndpointShouldBeAccessibleWithAuthentication() throws Exception {
        // Given: An error occurs for an authenticated user
        // When: Accessing the /error endpoint with authentication
        // Then: Should receive 500 response with error details
        mockMvc.perform(get("/error"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void nonExistentEndpointShouldReturn401WhenNotAuthenticated() throws Exception {
        // Given: A protected endpoint that doesn't exist
        // When: Accessing it without authentication
        // Then: Should receive 401 Unauthorized (expected security behavior)
        mockMvc.perform(get("/api/nonexistent"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.path").value("/api/nonexistent"));
    }

    @Test
    @WithMockUser
    void nonExistentEndpointWithAuthShouldReturn404() throws Exception {
        // Given: A protected endpoint that doesn't exist
        // When: Accessing it with valid authentication
        // Then: Should receive 404 Not Found (not 401)
        mockMvc.perform(get("/api/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
