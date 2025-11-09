# Foodify Performance Testing

This directory contains performance testing scenarios for the Foodify server using Gatling.

## Quick Start

### Prerequisites

1. Start the infrastructure services:
   ```bash
   docker compose up -d postgres kafka redis
   ```

2. Start the Foodify server:
   ```bash
   ./gradlew bootRun
   ```
   
   The server should be running at `http://localhost:8081`

3. Ensure you have test data loaded (users, restaurants, orders)

### Running Tests

#### Individual Test Types

Run a specific performance test scenario:

```bash
# Load Test (30 minutes, 200 users)
./gradlew runLoadTest

# Stress Test (20 minutes, progressive load to 2000+ users)
./gradlew runStressTest

# Endurance Test (2 hours by default, 100 users sustained)
./gradlew runEnduranceTest

# Spike Test (15 minutes, sudden spikes to 1200 users)
./gradlew runSpikeTest

# Run all performance tests
./gradlew runAllPerformanceTests
```

#### Custom Configuration

Override default test parameters using system properties:

```bash
# Custom base URL
./gradlew runLoadTest -Dperf.baseUrl=http://staging.foodify.com:8081

# Custom user count
./gradlew runLoadTest -Dperf.normalUsers=500

# Custom duration (in seconds)
./gradlew runLoadTest -Dperf.duration=3600 -Dperf.rampUp=600

# Longer endurance test (24 hours)
./gradlew runEnduranceTest -Dperf.endurance.hours=24

# Custom test credentials
./gradlew runLoadTest \
  -Dperf.testUserEmail=perf-test@foodify.com \
  -Dperf.testUserPassword=TestPass123
```

#### Combined Configuration Example

```bash
./gradlew runStressTest \
  -Dperf.baseUrl=http://staging.foodify.com:8081 \
  -Dperf.stressUsers=2000 \
  -Dperf.testUserEmail=test@foodify.com \
  -Dperf.testUserPassword=TestPass123
```

## Test Scenarios

### 1. Load Test (`LoadTestSimulation`)
**Purpose**: Verify normal peak load handling  
**Duration**: 30 minutes (configurable)  
**Users**: 200 concurrent (configurable)  
**Pattern**: Gradual ramp-up, sustained load, realistic user journeys  

**Scenarios**:
- 60% browsing restaurants
- 25% searching and filtering
- 15% checking orders

**Success Criteria**:
- p95 response time < 500ms
- p99 response time < 1000ms
- Error rate < 0.5%
- Average response time < 300ms

### 2. Stress Test (`StressTestSimulation`)
**Purpose**: Find breaking point and test graceful degradation  
**Duration**: 20 minutes  
**Users**: Progressive from 200 → 2000+ users  
**Pattern**: Staged increase every 3 minutes  

**Stages**:
1. Normal load (200 users)
2. Peak load (500 users)
3. Stress load (1000 users)
4. Extreme stress (2000 users)
5. Recovery ramp-down

**Success Criteria**:
- Identify maximum capacity
- p95 < 2s under stress
- p99 < 5s under stress
- Error rate < 5%
- No crashes or cascading failures

### 3. Endurance Test (`EnduranceTestSimulation`)
**Purpose**: Detect memory leaks and resource exhaustion over time  
**Duration**: 2-24 hours (configurable)  
**Users**: 100 concurrent (constant)  
**Pattern**: Sustained moderate load  

**Scenarios**:
- 70% continuous browsing
- 30% order checking
- Continuous health monitoring

**Success Criteria**:
- No performance degradation over time
- Stable memory usage
- p95 < 500ms remains consistent
- p99 < 1000ms remains consistent
- Error rate < 0.5%

### 4. Spike Test (`SpikeTestSimulation`)
**Purpose**: Test handling of sudden traffic surges  
**Duration**: 15 minutes  
**Users**: Sudden spikes: 50 → 300 → 50 → 600 → 50 → 1200 → 0  
**Pattern**: Instant spikes with recovery periods  

**Phases**:
1. Baseline (50 users)
2. Small spike (300 users for 2 min)
3. Recovery (50 users for 2 min)
4. Medium spike (600 users for 2 min)
5. Recovery (50 users for 2 min)
6. Large spike (1200 users for 2 min)
7. Final recovery

**Success Criteria**:
- System handles spikes without crashing
- p95 < 3s during spikes
- p99 < 5s during spikes
- Error rate < 5% during spikes
- Quick recovery to normal performance

## Viewing Results

After running tests, Gatling generates detailed HTML reports:

```bash
# Reports are saved in:
build/reports/gatling/<simulation-name>-<timestamp>/

# Open the latest report
open build/reports/gatling/$(ls -t build/reports/gatling/ | head -1)/index.html

# Or on Linux
xdg-open build/reports/gatling/$(ls -t build/reports/gatling/ | head -1)/index.html
```

The report includes:
- Global response time statistics
- Response time percentiles over time
- Requests per second
- Success/failure rates
- Detailed request statistics

## Monitoring During Tests

### Application Metrics
While tests are running, monitor application metrics:

```bash
# Prometheus metrics
curl http://localhost:8081/actuator/prometheus

# Health check
curl http://localhost:8081/actuator/health

# Thread dump
curl http://localhost:8081/actuator/threaddump
```

### System Resources

Monitor system resources during tests:

```bash
# CPU and memory
docker stats foodify-app

# Database connections
docker exec -it postgres psql -U foodify -d foodify -c "SELECT * FROM pg_stat_activity;"

# Redis statistics
docker exec -it redis redis-cli INFO stats
```

## Troubleshooting

### Connection Refused
If tests fail with connection errors:
1. Ensure the server is running: `curl http://localhost:8081/actuator/health`
2. Check the base URL: `-Dperf.baseUrl=http://localhost:8081`
3. Verify firewall/network settings

### Authentication Failures
If tests fail with 401/403 errors:
1. Verify test credentials exist
2. Check the login endpoint is accessible
3. Use custom credentials: `-Dperf.testUserEmail=user@test.com`

### Out of Memory
If Gatling runs out of memory:
1. Reduce concurrent users
2. Increase JVM heap: `JAVA_OPTS="-Xmx4g" ./gradlew runLoadTest`
3. Run tests on a machine with more RAM

### Server Overload
If the server becomes unresponsive:
1. Stop the test immediately
2. Check server logs for errors
3. Restart the server
4. Reduce test load or fix identified issues

## Best Practices

1. **Start Small**: Begin with load tests before stress tests
2. **Incremental**: Gradually increase load to find limits
3. **Consistent Environment**: Always test in the same environment
4. **Realistic Data**: Use production-like data volumes
5. **Monitor Everything**: Watch metrics, logs, and resources
6. **Document Results**: Keep records of test runs and findings
7. **Regular Testing**: Run performance tests regularly, not just before releases

## Integration with CI/CD

Add to your CI pipeline:

```yaml
# Example GitHub Actions workflow
performance-test:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v3
    - name: Start services
      run: docker compose up -d
    - name: Wait for services
      run: sleep 30
    - name: Run smoke test
      run: ./gradlew runLoadTest -Dperf.duration=300 -Dperf.normalUsers=50
    - name: Upload results
      uses: actions/upload-artifact@v3
      with:
        name: gatling-results
        path: build/reports/gatling/
```

## Additional Resources

- [Full Performance Testing Guide](../docs/PERFORMANCE_TESTING.md)
- [Gatling Documentation](https://gatling.io/docs/current/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

## Configuration Reference

### Available System Properties

| Property | Default | Description |
|----------|---------|-------------|
| `perf.baseUrl` | `http://localhost:8081` | Target server URL |
| `perf.testUserEmail` | `test@foodify.com` | Test user email |
| `perf.testUserPassword` | `password123` | Test user password |
| `perf.duration` | `1800` | Test duration in seconds |
| `perf.rampUp` | `300` | Ramp-up duration in seconds |
| `perf.normalUsers` | `200` | Normal load user count |
| `perf.peakUsers` | `500` | Peak load user count |
| `perf.stressUsers` | `1000` | Stress load user count |
| `perf.endurance.hours` | `2` | Endurance test duration in hours |
