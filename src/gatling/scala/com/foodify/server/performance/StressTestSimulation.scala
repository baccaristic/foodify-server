package com.foodify.server.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import PerformanceTestConfig._

/**
 * Stress Testing Simulation
 * 
 * Purpose: Determine the system's breaking point and how it recovers.
 * 
 * Scenario:
 * - Push system beyond normal operating capacity
 * - Identify bottlenecks and failure modes
 * - Verify graceful degradation
 * 
 * Success Criteria:
 * - Identify maximum capacity
 * - System degrades gracefully (no crashes)
 * - Quick recovery after load reduction
 * - Error messages are meaningful
 */
class StressTestSimulation extends Simulation {
  
  // Stress test parameters - progressively increase load
  private val stage1Users = normalLoadUsers // normal load
  private val stage2Users = peakLoadUsers // peak load
  private val stage3Users = stressLoadUsers // stress load
  private val stage4Users = stressLoadUsers * 2 // extreme stress
  
  private val stageRampTime = 2.minutes
  private val stageDuration = 3.minutes
  
  println(s"=== Stress Test Configuration ===")
  println(s"Base URL: $baseUrl")
  println(s"Stage 1 Users (Normal): $stage1Users")
  println(s"Stage 2 Users (Peak): $stage2Users")
  println(s"Stage 3 Users (Stress): $stage3Users")
  println(s"Stage 4 Users (Extreme): $stage4Users")
  println(s"Stage Duration: ${stageDuration.toSeconds} seconds")
  println(s"=================================")
  
  // Heavy load scenario - more aggressive than load test
  val stressScenario = scenario("Stress Load")
    .exec(login)
    .pause(1, 2)
    .repeat(10) {
      exec(getNearbyRestaurants)
        .pause(1)
        .exec(getTopRestaurants)
        .pause(1)
        .exec(searchRestaurants)
        .pause(1)
        .exec(getRestaurantDetails)
        .pause(1)
    }
  
  // Setup with progressive load increase
  setUp(
    stressScenario.inject(
      // Stage 1: Ramp to normal load
      rampUsers(stage1Users).during(stageRampTime),
      // Sustain normal load
      constantUsersPerSec(stage1Users / 60.0).during(stageDuration),
      
      // Stage 2: Ramp to peak load
      rampUsers(stage2Users - stage1Users).during(stageRampTime),
      // Sustain peak load
      constantUsersPerSec(stage2Users / 60.0).during(stageDuration),
      
      // Stage 3: Ramp to stress load
      rampUsers(stage3Users - stage2Users).during(stageRampTime),
      // Sustain stress load
      constantUsersPerSec(stage3Users / 60.0).during(stageDuration),
      
      // Stage 4: Push to breaking point
      rampUsers(stage4Users - stage3Users).during(stageRampTime),
      // Sustain extreme stress
      constantUsersPerSec(stage4Users / 60.0).during(stageDuration),
      
      // Recovery: Ramp down to zero
      rampUsers(0).during(2.minutes)
    )
  ).protocols(httpProtocol)
    .assertions(
      // More lenient assertions for stress test
      global.responseTime.percentile3.lt(2000), // p95 < 2s under stress
      global.responseTime.percentile4.lt(5000), // p99 < 5s under stress
      global.successfulRequests.percent.gt(95.0) // Allow up to 5% errors
    )
}
