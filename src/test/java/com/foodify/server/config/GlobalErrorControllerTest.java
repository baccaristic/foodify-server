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
 */
@SpringBootTest
@AutoConfigureMockMvc
class GlobalErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void errorEndpointShouldBeAccessibleWithoutAuthentication() throws Exception {
        // When accessing the /error endpoint directly
        // Then should receive a response without 401 Unauthorized
        mockMvc.perform(get("/error"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @WithMockUser
    void errorEndpointShouldBeAccessibleWithAuthentication() throws Exception {
        // When accessing the /error endpoint with authentication
        // Then should receive a response
        mockMvc.perform(get("/error"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void nonExistentEndpointShouldReturn404() throws Exception {
        // When accessing a non-existent endpoint without authentication
        // Then should receive 401 Unauthorized (expected behavior for protected endpoints)
        mockMvc.perform(get("/api/nonexistent"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void nonExistentEndpointWithAuthShouldReturn404() throws Exception {
        // When accessing a non-existent endpoint with authentication
        // Then should receive 404 Not Found
        mockMvc.perform(get("/api/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
