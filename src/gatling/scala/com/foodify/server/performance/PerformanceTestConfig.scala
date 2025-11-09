package com.foodify.server.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Base configuration and utilities for Foodify performance tests
 */
object PerformanceTestConfig {
  
  // Base URL configuration - can be overridden via environment variable
  val baseUrl: String = System.getProperty("perf.baseUrl", "http://localhost:8081")
  
  // Test user configuration
  val testUserEmail: String = System.getProperty("perf.testUserEmail", "test@foodify.com")
  val testUserPassword: String = System.getProperty("perf.testUserPassword", "password123")
  
  // Test duration configuration (in seconds)
  val testDuration: Int = System.getProperty("perf.duration", "1800").toInt // default 30 min
  val rampUpDuration: Int = System.getProperty("perf.rampUp", "300").toInt // default 5 min
  
  // Load test parameters
  val normalLoadUsers: Int = System.getProperty("perf.normalUsers", "200").toInt
  val peakLoadUsers: Int = System.getProperty("perf.peakUsers", "500").toInt
  val stressLoadUsers: Int = System.getProperty("perf.stressUsers", "1000").toInt
  
  // HTTP protocol configuration
  val httpProtocol = http
    .baseUrl(baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling Performance Test")
    // Add authentication token after login
    .shareConnections
  
  // Common headers
  val jsonHeaders = Map(
    "Content-Type" -> "application/json",
    "Accept" -> "application/json"
  )
  
  // Feeder for test data
  val restaurantIds = (1 to 100).iterator.map(i => Map("restaurantId" -> i))
  val orderIds = (1 to 1000).iterator.map(i => Map("orderId" -> i))
  
  // Common scenarios
  
  /**
   * Health check endpoint
   */
  val healthCheck = exec(
    http("Health Check")
      .get("/actuator/health")
      .check(status.is(200))
  )
  
  /**
   * Login and get JWT token
   */
  val login = exec(
    http("Login")
      .post("/api/auth/login")
      .body(StringBody(
        s"""{"email":"$testUserEmail","password":"$testUserPassword"}"""
      ))
      .asJson
      .check(status.is(200))
      .check(jsonPath("$.accessToken").saveAs("authToken"))
  ).pause(1)
  
  /**
   * Get nearby restaurants (client endpoint)
   */
  val getNearbyRestaurants = exec(
    http("Get Nearby Restaurants")
      .get("/api/client/nearby/restaurants")
      .queryParam("latitude", "36.8065")
      .queryParam("longitude", "10.1815")
      .queryParam("radius", "5000")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Get top restaurants
   */
  val getTopRestaurants = exec(
    http("Get Top Restaurants")
      .get("/api/client/nearby/top")
      .queryParam("latitude", "36.8065")
      .queryParam("longitude", "10.1815")
      .queryParam("radius", "5000")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Get favorite restaurants
   */
  val getFavoriteRestaurants = exec(
    http("Get Favorite Restaurants")
      .get("/api/client/nearby/favorites")
      .queryParam("latitude", "36.8065")
      .queryParam("longitude", "10.1815")
      .queryParam("radius", "5000")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Get restaurant details
   */
  val getRestaurantDetails = feed(restaurantIds).exec(
    http("Get Restaurant Details")
      .get("/api/restaurants/${restaurantId}")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.in(200, 404)) // 404 is acceptable if restaurant doesn't exist
  )
  
  /**
   * Search restaurants
   */
  val searchRestaurants = exec(
    http("Search Restaurants")
      .get("/api/restaurants/search")
      .queryParam("query", "pizza")
      .queryParam("latitude", "36.8065")
      .queryParam("longitude", "10.1815")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Get user orders
   */
  val getUserOrders = exec(
    http("Get User Orders")
      .get("/api/orders/user")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Get order details
   */
  val getOrderDetails = feed(orderIds).exec(
    http("Get Order Details")
      .get("/api/orders/${orderId}")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.in(200, 404)) // 404 is acceptable if order doesn't exist
  )
  
  /**
   * Get user profile
   */
  val getUserProfile = exec(
    http("Get User Profile")
      .get("/api/users/profile")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Get saved addresses
   */
  val getSavedAddresses = exec(
    http("Get Saved Addresses")
      .get("/api/addresses")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200))
  )
  
  /**
   * Metrics endpoint (for monitoring)
   */
  val getMetrics = exec(
    http("Get Metrics")
      .get("/actuator/prometheus")
      .check(status.is(200))
  )
  
  // Realistic user journey - typical client app usage
  val clientUserJourney = exec(
    login,
    pause(2),
    getNearbyRestaurants,
    pause(3),
    getTopRestaurants,
    pause(2),
    getFavoriteRestaurants,
    pause(5),
    getRestaurantDetails,
    pause(4),
    searchRestaurants,
    pause(3),
    getUserOrders,
    pause(2),
    getSavedAddresses
  )
  
  // Read-heavy scenario (browsing)
  val browsingScenario = exec(
    login,
    pause(1, 3),
    getNearbyRestaurants,
    pause(2, 4),
    getTopRestaurants,
    pause(1, 3),
    getRestaurantDetails,
    pause(3, 6),
    searchRestaurants,
    pause(2, 4),
    getRestaurantDetails
  )
  
  // Mixed scenario (realistic mix of read/write)
  val mixedScenario = exec(
    login,
    pause(1, 2),
    getNearbyRestaurants,
    pause(2, 3),
    getRestaurantDetails,
    pause(3, 5),
    getUserOrders,
    pause(2, 4),
    getOrderDetails,
    pause(1, 2),
    getSavedAddresses
  )
}
