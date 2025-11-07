package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.RestaurantSpecialDay;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantOperatingStatusServiceTest {

    private RestaurantOperatingStatusService service;

    @BeforeEach
    void setUp() {
        service = new RestaurantOperatingStatusService();
    }

    @Test
    void isRestaurantOpen_whenWithinOperatingHours_returnsTrue() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true, 
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1); // This is a Monday
        LocalTime currentTime = LocalTime.of(12, 0); // Noon

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertTrue(isOpen);
    }

    @Test
    void isRestaurantOpen_whenBeforeOpeningTime_returnsFalse() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(8, 0); // Before opening

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertFalse(isOpen);
    }

    @Test
    void isRestaurantOpen_whenAfterClosingTime_returnsFalse() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(23, 0); // After closing

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertFalse(isOpen);
    }

    @Test
    void isRestaurantOpen_whenMarkedAsClosed_returnsFalse() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, false, null, null);
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(12, 0);

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertFalse(isOpen);
    }

    @Test
    void isRestaurantOpen_whenCrossesMidnight_returnsTrue() {
        // Arrange - Restaurant open from 22:00 to 02:00
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(22, 0), LocalTime.of(2, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(23, 30); // 11:30 PM

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertTrue(isOpen);
    }

    @Test
    void isRestaurantOpen_whenCrossesMidnightAndAfterMidnight_returnsTrue() {
        // Arrange - Restaurant open from 22:00 to 02:00
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(22, 0), LocalTime.of(2, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(1, 0); // 1:00 AM (next day)

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertTrue(isOpen);
    }

    @Test
    void isRestaurantOpen_withSpecialDayOverride_closedSpecialDay_returnsFalse() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        RestaurantSpecialDay specialDay = createSpecialDay("Holiday", monday, false, null, null);
        List<RestaurantSpecialDay> specialDays = List.of(specialDay);
        
        LocalTime currentTime = LocalTime.of(12, 0); // Normally would be open

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertFalse(isOpen);
    }

    @Test
    void isRestaurantOpen_withSpecialDayOverride_differentHours_usesSpecialDayHours() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        // Special day: open 10:00-18:00
        RestaurantSpecialDay specialDay = createSpecialDay("Special Event", monday, true,
                LocalTime.of(10, 0), LocalTime.of(18, 0));
        List<RestaurantSpecialDay> specialDays = List.of(specialDay);
        
        LocalTime currentTime = LocalTime.of(9, 30); // Would be open on regular day, but not on special day

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertFalse(isOpen);
    }

    @Test
    void isRestaurantOpen_withSpecialDayOverride_withinSpecialHours_returnsTrue() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        RestaurantSpecialDay specialDay = createSpecialDay("Special Event", monday, true,
                LocalTime.of(10, 0), LocalTime.of(18, 0));
        List<RestaurantSpecialDay> specialDays = List.of(specialDay);
        
        LocalTime currentTime = LocalTime.of(14, 0); // Within special day hours

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertTrue(isOpen);
    }

    @Test
    void getOperatingHoursForDate_normalDay_returnsWeeklyHours() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);

        // Act
        RestaurantOperatingStatusService.OperatingHours result = 
                service.getOperatingHoursForDate(weeklyHours, specialDays, monday);

        // Assert
        assertNotNull(result);
        assertEquals(LocalTime.of(9, 0), result.opensAt());
        assertEquals(LocalTime.of(22, 0), result.closesAt());
    }

    @Test
    void getOperatingHoursForDate_specialDay_returnsSpecialDayHours() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        RestaurantSpecialDay specialDay = createSpecialDay("Special Event", monday, true,
                LocalTime.of(10, 0), LocalTime.of(18, 0));
        List<RestaurantSpecialDay> specialDays = List.of(specialDay);

        // Act
        RestaurantOperatingStatusService.OperatingHours result = 
                service.getOperatingHoursForDate(weeklyHours, specialDays, monday);

        // Assert
        assertNotNull(result);
        assertEquals(LocalTime.of(10, 0), result.opensAt());
        assertEquals(LocalTime.of(18, 0), result.closesAt());
    }

    @Test
    void getOperatingHoursForDate_closedDay_returnsNull() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, false, null, null);
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);

        // Act
        RestaurantOperatingStatusService.OperatingHours result = 
                service.getOperatingHoursForDate(weeklyHours, specialDays, monday);

        // Assert
        assertNull(result);
    }

    @Test
    void getOperatingHoursForDate_closedSpecialDay_returnsNull() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        RestaurantSpecialDay specialDay = createSpecialDay("Holiday", monday, false, null, null);
        List<RestaurantSpecialDay> specialDays = List.of(specialDay);

        // Act
        RestaurantOperatingStatusService.OperatingHours result = 
                service.getOperatingHoursForDate(weeklyHours, specialDays, monday);

        // Assert
        assertNull(result);
    }

    @Test
    void isRestaurantOpen_atOpeningTime_returnsTrue() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(9, 0); // Exactly at opening time

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertTrue(isOpen);
    }

    @Test
    void isRestaurantOpen_atClosingTime_returnsFalse() {
        // Arrange
        RestaurantWeeklyOperatingHour hour = createWeeklyHour(DayOfWeek.MONDAY, true,
                LocalTime.of(9, 0), LocalTime.of(22, 0));
        List<RestaurantWeeklyOperatingHour> weeklyHours = List.of(hour);
        List<RestaurantSpecialDay> specialDays = List.of();
        
        LocalDate monday = LocalDate.of(2024, 1, 1);
        LocalTime currentTime = LocalTime.of(22, 0); // Exactly at closing time

        // Act
        boolean isOpen = service.isRestaurantOpen(weeklyHours, specialDays, monday, currentTime);

        // Assert
        assertFalse(isOpen);
    }

    // Helper methods
    private RestaurantWeeklyOperatingHour createWeeklyHour(DayOfWeek day, boolean open, 
                                                           LocalTime opensAt, LocalTime closesAt) {
        RestaurantWeeklyOperatingHour hour = new RestaurantWeeklyOperatingHour();
        hour.setDayOfWeek(day);
        hour.setOpen(open);
        hour.setOpensAt(opensAt);
        hour.setClosesAt(closesAt);
        return hour;
    }

    private RestaurantSpecialDay createSpecialDay(String name, LocalDate date, boolean open,
                                                  LocalTime opensAt, LocalTime closesAt) {
        RestaurantSpecialDay day = new RestaurantSpecialDay();
        day.setName(name);
        day.setDate(date);
        day.setOpen(open);
        day.setOpensAt(opensAt);
        day.setClosesAt(closesAt);
        return day;
    }
}
