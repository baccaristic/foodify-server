# Performance Testing Quick Reference

## TL;DR - Run Your First Test

```bash
# 1. Start the server
docker compose up -d
./gradlew bootRun

# 2. Run a 5-minute smoke test
./performance-test.sh smoke

# 3. View the results
open build/reports/gatling/*/index.html
```

## Common Commands

| What You Want | Command |
|---------------|---------|
| Quick validation (5 min) | `./performance-test.sh smoke` |
| Normal load test (30 min) | `./performance-test.sh load` |
| Find breaking point | `./performance-test.sh stress` |
| Check for memory leaks (2 hrs) | `./performance-test.sh endurance` |
| Test spike handling | `./performance-test.sh spike` |
| Custom target | `./performance-test.sh load --url http://staging:8081` |
| More users | `./gradlew runLoadTest -Dperf.normalUsers=500` |
| Longer test | `./gradlew runLoadTest -Dperf.duration=3600` |

## Test Types at a Glance

### 🟢 Smoke Test
**When**: After every deployment, before deeper testing  
**Duration**: 5 minutes  
**Load**: 50 users  
**Purpose**: Quick sanity check  

### 🔵 Load Test
**When**: Weekly, before releases  
**Duration**: 30 minutes  
**Load**: 200 concurrent users  
**Purpose**: Verify normal peak performance  
**Success**: p95 < 500ms, p99 < 1s, errors < 0.1%

### 🟡 Stress Test
**When**: Bi-weekly, capacity planning  
**Duration**: 20 minutes  
**Load**: Progressive to 2000+ users  
**Purpose**: Find breaking point  
**Success**: Graceful degradation, no crashes

### 🟠 Endurance Test
**When**: Monthly, before major releases  
**Duration**: 2-24 hours  
**Load**: 100 users sustained  
**Purpose**: Detect memory leaks  
**Success**: Stable memory, consistent performance

### 🔴 Spike Test
**When**: Before events, flash sales  
**Duration**: 15 minutes  
**Load**: Sudden spikes to 1200 users  
**Purpose**: Verify spike resilience  
**Success**: No crashes, quick recovery

## Configuration Parameters

```bash
# All tests support these parameters
-Dperf.baseUrl=http://localhost:8081     # Target URL
-Dperf.testUserEmail=test@foodify.com    # Test user
-Dperf.testUserPassword=password123      # Password
-Dperf.duration=1800                     # Duration (seconds)
-Dperf.rampUp=300                        # Ramp-up (seconds)
-Dperf.normalUsers=200                   # Load test users
-Dperf.peakUsers=500                     # Peak users
-Dperf.stressUsers=1000                  # Stress users
-Dperf.endurance.hours=2                 # Endurance duration
```

## Performance Targets

| Metric | Target | Critical |
|--------|--------|----------|
| Response Time (p95) | < 500ms | < 1000ms |
| Response Time (p99) | < 1000ms | < 2000ms |
| Error Rate | < 0.1% | < 1% |
| Throughput | 100+ RPS | 50+ RPS |
| CPU Usage | < 70% | < 85% |
| Memory Usage | < 80% | < 90% |

## Monitoring During Tests

```bash
# Application metrics
curl http://localhost:8081/actuator/prometheus | grep http_server_requests

# Health check
curl http://localhost:8081/actuator/health

# Thread dump
curl http://localhost:8081/actuator/threaddump

# Database connections
docker exec postgres psql -U foodify -d foodify -c "SELECT * FROM pg_stat_activity;"

# System resources
docker stats foodify-app
```

## Understanding Reports

Open `build/reports/gatling/*/index.html` after a test run.

**Key sections:**
1. **Global Stats** - Overall summary (look here first)
2. **Response Time Distribution** - Histogram (should be left-skewed)
3. **Response Time Percentiles** - Trends over time (should be flat)
4. **Requests/Responses per Second** - Throughput (should match load)
5. **Active Users** - Load profile

**Good indicators:**
- ✅ Response times stay flat as load increases
- ✅ Throughput scales with users
- ✅ Error count near zero
- ✅ No spikes or anomalies

**Warning signs:**
- ⚠️ Response times trending upward
- ⚠️ Throughput plateauing early
- ⚠️ Growing error rate
- ⚠️ Sudden response time spikes

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Connection refused | Check server is running: `curl http://localhost:8081/actuator/health` |
| 401 Unauthorized | Verify test user exists and credentials are correct |
| High response times | Check database query performance, add indexes |
| Memory growing | Look for memory leaks with heap dump analysis |
| Timeouts | Check thread pool size, connection pool limits |
| Low throughput | Check for blocking operations, optimize queries |

## Performance Environment

For production-like testing:

```bash
# Start performance-tuned environment
docker compose -f docker-compose.performance.yml up -d

# App runs on port 8082
./performance-test.sh load --url http://localhost:8082

# Access monitoring
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3000 (admin/admin)
```

## CI/CD Integration

```yaml
# GitHub Actions example
- name: Performance Smoke Test
  run: |
    docker compose up -d
    sleep 30
    ./performance-test.sh smoke
    
- name: Upload Reports
  uses: actions/upload-artifact@v3
  with:
    name: gatling-results
    path: build/reports/gatling/
```

## Need Help?

1. **Full Guide**: [docs/PERFORMANCE_TESTING.md](../docs/PERFORMANCE_TESTING.md)
2. **Gatling Docs**: [src/gatling/README.md](README.md)
3. **Gatling Official**: https://gatling.io/docs/

## Data Setup

Before running tests, ensure you have:
- ✅ Test user created (test@foodify.com / password123)
- ✅ Sample restaurants (at least 100)
- ✅ Sample orders (at least 1000)

See `src/gatling/resources/README.md` for data setup instructions.
