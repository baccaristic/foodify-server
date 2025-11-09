package com.foodify.server.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import PerformanceTestConfig._

/**
 * Spike Testing Simulation
 * 
 * Purpose: Assess the system's ability to handle sudden traffic spikes gracefully.
 * 
 * Scenario:
 * - Simulate sudden, extreme increases in user load
 * - Test auto-scaling and resource allocation
 * - Verify system stability during spikes
 * 
 * Pattern:
 * - Baseline → Spike → Recovery → Bigger Spike → Recovery
 * - Each spike lasts 2 minutes
 * - Recovery periods of 2 minutes between spikes
 * 
 * Success Criteria:
 * - System handles spikes without crashing
 * - Response times return to normal quickly
 * - No cascading failures
 * - Error rate acceptable during spike (< 5%)
 */
class SpikeTestSimulation extends Simulation {
  
  // Spike test configuration
  private val baselineUsers = 50 // Normal baseline
  private val smallSpikeUsers = 300 // First spike
  private val mediumSpikeUsers = 600 // Second spike
  private val largeSpikeUsers = 1200 // Third spike
  
  private val spikeDuration = 2.minutes
  private val recoveryDuration = 2.minutes
  private val instantSpike = 10.seconds // Very fast ramp-up
  
  println(s"=== Spike Test Configuration ===")
  println(s"Base URL: $baseUrl")
  println(s"Baseline Users: $baselineUsers")
  println(s"Small Spike Users: $smallSpikeUsers")
  println(s"Medium Spike Users: $mediumSpikeUsers")
  println(s"Large Spike Users: $largeSpikeUsers")
  println(s"Spike Duration: ${spikeDuration.toSeconds} seconds")
  println(s"Recovery Duration: ${recoveryDuration.toSeconds} seconds")
  println(s"================================")
  
  // Aggressive scenario for spike testing
  val spikeScenario = scenario("Spike Load")
    .exec(login)
    .pause(1)
    .repeat(5) {
      exec(getNearbyRestaurants)
        .pause(1, 2)
        .exec(getRestaurantDetails)
        .pause(1, 2)
    }
  
  setUp(
    spikeScenario.inject(
      // Phase 1: Baseline load
      rampUsers(baselineUsers).during(1.minute),
      constantUsersPerSec(baselineUsers / 30.0).during(recoveryDuration),
      
      // Phase 2: Small spike
      rampUsers(smallSpikeUsers).during(instantSpike),
      constantUsersPerSec(smallSpikeUsers / 30.0).during(spikeDuration),
      
      // Recovery to baseline
      rampUsers(-smallSpikeUsers + baselineUsers).during(instantSpike),
      constantUsersPerSec(baselineUsers / 30.0).during(recoveryDuration),
      
      // Phase 3: Medium spike
      rampUsers(mediumSpikeUsers).during(instantSpike),
      constantUsersPerSec(mediumSpikeUsers / 30.0).during(spikeDuration),
      
      // Recovery to baseline
      rampUsers(-mediumSpikeUsers + baselineUsers).during(instantSpike),
      constantUsersPerSec(baselineUsers / 30.0).during(recoveryDuration),
      
      // Phase 4: Large spike
      rampUsers(largeSpikeUsers).during(instantSpike),
      constantUsersPerSec(largeSpikeUsers / 30.0).during(spikeDuration),
      
      // Final recovery
      rampUsers(-largeSpikeUsers).during(1.minute)
    )
  ).protocols(httpProtocol)
    .assertions(
      // During spikes, allow higher response times and some errors
      global.responseTime.percentile3.lt(3000), // p95 < 3s
      global.responseTime.percentile4.lt(5000), // p99 < 5s
      global.successfulRequests.percent.gt(95.0), // Allow up to 5% errors during spikes
      // Ensure system doesn't crash
      global.failedRequests.count.lt((baselineUsers + smallSpikeUsers + mediumSpikeUsers + largeSpikeUsers) * 20)
    )
}
