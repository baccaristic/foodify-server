package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.RestaurantSpecialDay;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Service to determine restaurant operating status based on weekly schedule and special days.
 */
@Service
@RequiredArgsConstructor
public class RestaurantOperatingStatusService {

    /**
     * Determines if a restaurant is currently open based on weekly schedule and special days.
     * Special days override weekly schedule for specific dates.
     *
     * @param weeklyHours List of weekly operating hours
     * @param specialDays List of special days
     * @param currentDate Current date
     * @param currentTime Current time
     * @return true if restaurant is open, false otherwise
     */
    public boolean isRestaurantOpen(
            List<RestaurantWeeklyOperatingHour> weeklyHours,
            List<RestaurantSpecialDay> specialDays,
            LocalDate currentDate,
            LocalTime currentTime) {
        
        // Check if there's a special day override for the current date
        Optional<RestaurantSpecialDay> specialDay = specialDays.stream()
                .filter(sd -> sd.getDate().equals(currentDate))
                .findFirst();
        
        if (specialDay.isPresent()) {
            RestaurantSpecialDay day = specialDay.get();
            if (!day.isOpen()) {
                return false;
            }
            if (day.getOpensAt() != null && day.getClosesAt() != null) {
                return isWithinTimeRange(currentTime, day.getOpensAt(), day.getClosesAt());
            }
            // If special day is marked as open but no hours specified, consider it open
            return true;
        }
        
        // Check weekly schedule
        DayOfWeek currentDay = currentDate.getDayOfWeek();
        Optional<RestaurantWeeklyOperatingHour> weeklyHour = weeklyHours.stream()
                .filter(wh -> wh.getDayOfWeek().equals(currentDay))
                .findFirst();
        
        if (weeklyHour.isEmpty()) {
            return false;
        }
        
        RestaurantWeeklyOperatingHour hour = weeklyHour.get();
        if (!hour.isOpen()) {
            return false;
        }
        
        if (hour.getOpensAt() != null && hour.getClosesAt() != null) {
            return isWithinTimeRange(currentTime, hour.getOpensAt(), hour.getClosesAt());
        }
        
        // If marked as open but no hours specified, consider it open
        return true;
    }
    
    /**
     * Gets the opening and closing hours for the current day, considering special days.
     *
     * @param weeklyHours List of weekly operating hours
     * @param specialDays List of special days
     * @param currentDate Current date
     * @return OperatingHours with opening and closing times, or null if closed
     */
    public OperatingHours getOperatingHoursForDate(
            List<RestaurantWeeklyOperatingHour> weeklyHours,
            List<RestaurantSpecialDay> specialDays,
            LocalDate currentDate) {
        
        // Check if there's a special day override for the current date
        Optional<RestaurantSpecialDay> specialDay = specialDays.stream()
                .filter(sd -> sd.getDate().equals(currentDate))
                .findFirst();
        
        if (specialDay.isPresent()) {
            RestaurantSpecialDay day = specialDay.get();
            if (!day.isOpen()) {
                return null;
            }
            return new OperatingHours(day.getOpensAt(), day.getClosesAt());
        }
        
        // Check weekly schedule
        DayOfWeek currentDay = currentDate.getDayOfWeek();
        Optional<RestaurantWeeklyOperatingHour> weeklyHour = weeklyHours.stream()
                .filter(wh -> wh.getDayOfWeek().equals(currentDay))
                .findFirst();
        
        if (weeklyHour.isEmpty()) {
            return null;
        }
        
        RestaurantWeeklyOperatingHour hour = weeklyHour.get();
        if (!hour.isOpen()) {
            return null;
        }
        
        return new OperatingHours(hour.getOpensAt(), hour.getClosesAt());
    }
    
    /**
     * Checks if the current time is within the specified time range.
     * Handles cases where closing time is before opening time (crosses midnight).
     */
    private boolean isWithinTimeRange(LocalTime currentTime, LocalTime opensAt, LocalTime closesAt) {
        if (opensAt == null || closesAt == null) {
            return true;
        }
        
        // Handle case where business hours cross midnight (e.g., 22:00 to 02:00)
        if (closesAt.isBefore(opensAt)) {
            // If we're after opening time or before closing time
            return currentTime.isAfter(opensAt) || currentTime.isBefore(closesAt) || 
                   currentTime.equals(opensAt);
        }
        
        // Normal case: opening time is before closing time
        return (currentTime.isAfter(opensAt) || currentTime.equals(opensAt)) && 
               currentTime.isBefore(closesAt);
    }
    
    /**
     * Record to hold operating hours for a specific date.
     */
    public record OperatingHours(LocalTime opensAt, LocalTime closesAt) {
        public String getOpeningHoursFormatted() {
            return opensAt != null ? opensAt.toString() : null;
        }
        
        public String getClosingHoursFormatted() {
            return closesAt != null ? closesAt.toString() : null;
        }
    }
}
