package com.foodify.server.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import PerformanceTestConfig._

/**
 * Load Testing Simulation
 * 
 * Purpose: Verify the system can handle typical high traffic without performance degradation.
 * 
 * Scenario:
 * - Ramp up users gradually to expected peak load
 * - Sustain peak load for 10-30 minutes
 * - Monitor all key metrics
 * 
 * Success Criteria:
 * - Response time p95 < 500ms
 * - Response time p99 < 1000ms
 * - Error rate < 0.1%
 * - System remains stable
 */
class LoadTestSimulation extends Simulation {
  
  // Test configuration
  private val numberOfUsers = normalLoadUsers
  private val rampUpTime = rampUpDuration.seconds
  private val duration = testDuration.seconds
  
  println(s"=== Load Test Configuration ===")
  println(s"Base URL: $baseUrl")
  println(s"Number of Users: $numberOfUsers")
  println(s"Ramp Up Time: ${rampUpTime.toSeconds} seconds")
  println(s"Test Duration: ${duration.toSeconds} seconds")
  println(s"==============================")
  
  // Define scenarios with realistic distribution
  val clientBrowsingScenario = scenario("Client Browsing")
    .exec(browsingScenario)
    .pause(5, 10)
    .repeat(3) {
      exec(getRestaurantDetails)
        .pause(2, 4)
    }
  
  val clientSearchScenario = scenario("Client Searching")
    .exec(login)
    .pause(2)
    .repeat(5) {
      exec(searchRestaurants)
        .pause(3, 5)
        .exec(getRestaurantDetails)
        .pause(2, 4)
    }
  
  val clientOrderCheckScenario = scenario("Client Order Check")
    .exec(login)
    .pause(1)
    .repeat(3) {
      exec(getUserOrders)
        .pause(2, 4)
        .exec(getOrderDetails)
        .pause(3, 5)
    }
  
  val healthCheckScenario = scenario("Health Monitoring")
    .repeat(duration.toSeconds.toInt / 30) {
      exec(healthCheck)
        .pause(30)
    }
  
  // Set up the simulation with realistic load distribution
  setUp(
    // 60% browsing (most common activity)
    clientBrowsingScenario.inject(
      rampUsers(numberOfUsers * 60 / 100).during(rampUpTime)
    ),
    
    // 25% searching
    clientSearchScenario.inject(
      rampUsers(numberOfUsers * 25 / 100).during(rampUpTime)
    ),
    
    // 15% checking orders
    clientOrderCheckScenario.inject(
      rampUsers(numberOfUsers * 15 / 100).during(rampUpTime)
    ),
    
    // Continuous health checks
    healthCheckScenario.inject(
      atOnceUsers(1)
    )
  ).protocols(httpProtocol)
    .maxDuration(duration + rampUpTime)
    .assertions(
      global.responseTime.percentile3.lt(500), // p95 < 500ms
      global.responseTime.percentile4.lt(1000), // p99 < 1000ms
      global.successfulRequests.percent.gt(99.5), // < 0.5% error rate
      global.responseTime.mean.lt(300) // average < 300ms
    )
}
