package com.foodify.server.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import PerformanceTestConfig._

/**
 * Endurance (Soak) Testing Simulation
 * 
 * Purpose: Identify memory leaks or resource exhaustion issues over time.
 * 
 * Scenario:
 * - Sustain typical load for extended period
 * - Monitor for gradual performance degradation
 * - Check for memory leaks and resource cleanup
 * 
 * Configuration:
 * - Default: 2 hours (can be extended to 24+ hours)
 * - Constant load throughout
 * 
 * Success Criteria:
 * - No performance degradation over time
 * - Memory usage stable (no leaks)
 * - No connection pool exhaustion
 * - Response times remain consistent
 */
class EnduranceTestSimulation extends Simulation {
  
  // Endurance test configuration
  private val numberOfUsers = normalLoadUsers / 2 // Moderate sustained load
  private val rampUpTime = 5.minutes
  // Default 2 hours, but can be configured for longer
  private val testDurationHours = System.getProperty("perf.endurance.hours", "2").toInt
  private val duration = testDurationHours.hours
  
  println(s"=== Endurance Test Configuration ===")
  println(s"Base URL: $baseUrl")
  println(s"Number of Users: $numberOfUsers")
  println(s"Ramp Up Time: ${rampUpTime.toMinutes} minutes")
  println(s"Test Duration: ${duration.toHours} hours (${duration.toMinutes} minutes)")
  println(s"====================================")
  
  // Realistic sustained browsing pattern
  val enduranceBrowsingScenario = scenario("Sustained Browsing")
    .forever {
      exec(browsingScenario)
        .pause(5, 15)
    }
  
  // Realistic sustained order checking pattern
  val enduranceOrderScenario = scenario("Sustained Order Checking")
    .forever {
      exec(login)
        .pause(2, 4)
        .exec(getUserOrders)
        .pause(3, 6)
        .exec(getOrderDetails)
        .pause(5, 10)
    }
  
  // Periodic metrics collection
  val enduranceMonitoringScenario = scenario("Continuous Monitoring")
    .forever {
      exec(healthCheck)
        .pause(30)
        .exec(getMetrics)
        .pause(30)
    }
  
  setUp(
    // 70% browsing
    enduranceBrowsingScenario.inject(
      rampUsers(numberOfUsers * 70 / 100).during(rampUpTime)
    ),
    
    // 30% order checking
    enduranceOrderScenario.inject(
      rampUsers(numberOfUsers * 30 / 100).during(rampUpTime)
    ),
    
    // Monitoring
    enduranceMonitoringScenario.inject(
      atOnceUsers(1)
    )
  ).protocols(httpProtocol)
    .maxDuration(duration + rampUpTime)
    .assertions(
      // Assertions for stable performance over time
      global.responseTime.percentile3.lt(500), // p95 should remain < 500ms
      global.responseTime.percentile4.lt(1000), // p99 should remain < 1000ms
      global.successfulRequests.percent.gt(99.5), // < 0.5% error rate
      // Mean response time should not degrade
      global.responseTime.mean.lt(300)
    )
    .throttle(
      // Maintain steady request rate to detect performance degradation
      reachRps(50).in(rampUpTime),
      holdFor(duration)
    )
}
