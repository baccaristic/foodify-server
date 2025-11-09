package com.foodify.server.modules.admin.driver.api;

import com.foodify.server.modules.admin.driver.application.AdminDriverService;
import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.DriverListItemDto;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.identity.domain.Driver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminDriverController.class)
class AdminDriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminDriverService adminDriverService;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getDrivers_shouldReturnPagedDrivers() throws Exception {
        // Given
        DriverListItemDto driver = new DriverListItemDto(1L, "Test Driver", "1234567890", DriverDepositStatus.CONFIRMED);
        Page<DriverListItemDto> page = new PageImpl<>(Arrays.asList(driver));
        
        when(adminDriverService.getDriverList(any(), any(), anyInt(), anyInt()))
                .thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers")
                .with(csrf())
                .param("query", "test")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Test Driver")))
                .andExpect(jsonPath("$.content[0].phone", is("1234567890")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getDriver_shouldReturnDriverDetails() throws Exception {
        // Given
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("Test Driver");
        driver.setPhone("1234567890");
        
        when(adminDriverService.getDriverById(1L)).thenReturn(driver);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Driver")))
                .andExpect(jsonPath("$.phone", is("1234567890")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getDriverStatistics_shouldReturnStatistics() throws Exception {
        // Given
        when(adminDriverService.getDriverRating(anyLong(), any(), any()))
                .thenReturn(4.5);
        when(adminDriverService.getDriverOnTimePercentage(anyLong(), any(), any()))
                .thenReturn(85.5);
        when(adminDriverService.getDriverAverageDeliveryTime(anyLong(), any(), any()))
                .thenReturn(25.5);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers/1/statistics")
                .with(csrf())
                .param("startDate", "2024-01-01T00:00:00")
                .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId", is(1)))
                .andExpect(jsonPath("$.averageRating", is(4.5)))
                .andExpect(jsonPath("$.onTimePercentage", is(85.5)))
                .andExpect(jsonPath("$.averageDeliveryTimeMinutes", is(25.5)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getDriverRating_shouldReturnRating() throws Exception {
        // Given
        when(adminDriverService.getDriverRating(anyLong(), any(), any()))
                .thenReturn(4.5);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers/1/rating")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(4.5)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getDailyRatings_shouldReturnDailyRatings() throws Exception {
        // Given
        List<DailyRatingDto> dailyRatings = Arrays.asList(
                new DailyRatingDto(LocalDate.of(2024, 1, 1), 4.5),
                new DailyRatingDto(LocalDate.of(2024, 1, 2), 4.8)
        );
        
        when(adminDriverService.getDriverDailyRatings(anyLong(), any(), any()))
                .thenReturn(dailyRatings);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers/1/daily-ratings")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2024-01-01")))
                .andExpect(jsonPath("$[0].avgRating", is(4.5)))
                .andExpect(jsonPath("$[1].date", is("2024-01-02")))
                .andExpect(jsonPath("$[1].avgRating", is(4.8)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getDailyOnTimePercentage_shouldReturnDailyData() throws Exception {
        // Given
        List<DailyOnTimePercentageDto> dailyData = Arrays.asList(
                new DailyOnTimePercentageDto(LocalDate.of(2024, 1, 1), 90.0),
                new DailyOnTimePercentageDto(LocalDate.of(2024, 1, 2), 85.0)
        );
        
        when(adminDriverService.getDriverDailyOnTimePercentage(anyLong(), any(), any()))
                .thenReturn(dailyData);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers/1/daily-on-time")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date", is("2024-01-01")))
                .andExpect(jsonPath("$[0].onTimePercentage", is(90.0)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getRatingDistribution_shouldReturnDistribution() throws Exception {
        // Given
        List<Long> distribution = Arrays.asList(5L, 10L, 15L, 30L, 40L);
        
        when(adminDriverService.getDriverRatingDistribution(anyLong(), any(), any()))
                .thenReturn(distribution);

        // When & Then
        mockMvc.perform(get("/api/admin/drivers/1/rating-distribution")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId", is(1)))
                .andExpect(jsonPath("$.distribution", hasSize(5)))
                .andExpect(jsonPath("$.distribution[0]", is(5)))
                .andExpect(jsonPath("$.distribution[4]", is(40)));
    }

    @Test
    void getDrivers_shouldReturnRedirectWithoutAuth() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/admin/drivers"))
                .andExpect(status().is3xxRedirection());
    }
}
