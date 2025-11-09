# Performance Testing Guide for Foodify Server

## Overview

This document outlines the performance testing strategy, key metrics, and procedures for the Foodify delivery app backend. Performance testing ensures the system can handle expected load and identifies bottlenecks before they impact production.

## Key Performance Metrics

### 1. Response Time / API Latency
Time taken for an API request to be processed and a response sent.

**Target Metrics:**
- **Average Response Time**: < 200ms for most endpoints
- **95th Percentile (p95)**: < 500ms
- **99th Percentile (p99)**: < 1000ms
- **Max Response Time**: < 3000ms

### 2. Throughput (Requests Per Second - RPS)
The number of requests the server can handle per second.

**Target Metrics:**
- **Normal Load**: 100-200 RPS sustained
- **Peak Load**: 500-1000 RPS sustained
- **Burst Capacity**: 2000+ RPS for short periods

### 3. Error Rate
The percentage of requests that return server errors (HTTP 5xx status codes).

**Target Metrics:**
- **Normal Load**: < 0.1% error rate
- **Peak Load**: < 1% error rate
- **Stress Conditions**: Graceful degradation (no cascading failures)

### 4. Resource Utilization
CPU and memory usage during various load scenarios.

**Target Metrics:**
- **CPU Usage**: < 70% under normal load, < 85% under peak load
- **Memory Usage**: < 80% of heap, no memory leaks over time
- **Thread Pool**: < 80% utilization
- **Database Connections**: < 70% of pool capacity

### 5. Database Query Performance
Average query duration and number of slow queries.

**Target Metrics:**
- **Average Query Time**: < 50ms
- **Slow Query Count**: < 5% of queries > 100ms
- **Connection Pool**: No connection exhaustion

### 6. Delivery-Specific Metrics
Key business metrics for the delivery platform.

**Target Metrics:**
- **Order Processing Time**: < 500ms from submission to confirmation
- **Location Update Latency**: < 200ms for driver tracking
- **Restaurant Search Response**: < 300ms
- **Real-time Updates**: < 1s for WebSocket notifications

## Production-Like Environment Requirements

To ensure accurate performance testing results:

### Infrastructure
- **Environment**: Use staging or pre-production matching production
- **Hardware**: Similar CPU, memory, and disk I/O characteristics
- **Network**: Comparable latency and bandwidth
- **Database**: Real PostgreSQL (not H2), with production-like data volume

### Data Setup
- **Volume**: Minimum 10,000 users, 100 restaurants, 50,000+ orders
- **Distribution**: Representative geographic spread
- **Anonymization**: Use anonymized production data when possible
- **Realistic Payloads**: Match production request/response sizes

### Configuration
- **Connection Pools**: Match production settings
- **Cache Configuration**: Redis with production-like TTL
- **Async Processing**: Kafka topics configured as in production
- **Security**: OAuth/JWT enabled as in production

## Performance Test Types

### 1. Load Testing
**Purpose**: Verify the system can handle typical high traffic without performance degradation.

**Scenario**:
- Ramp up users gradually to expected peak load
- Sustain peak load for 10-30 minutes
- Monitor all key metrics

**Configuration**:
- Duration: 30 minutes
- Users: 100-500 concurrent users
- Ramp-up: 5 minutes
- Target RPS: 200-500 requests/second

**Success Criteria**:
- All response time targets met
- Error rate < 0.1%
- No resource exhaustion
- System remains stable

### 2. Stress Testing
**Purpose**: Determine the system's breaking point and how it recovers.

**Scenario**:
- Push system beyond normal operating capacity
- Identify bottlenecks and failure modes
- Verify graceful degradation

**Configuration**:
- Duration: 20 minutes
- Users: Ramp to 2000+ concurrent users
- Ramp-up: Gradual increase every 2 minutes
- Target RPS: Push to 1000+ requests/second

**Success Criteria**:
- Identify maximum capacity
- System degrades gracefully (no crashes)
- Quick recovery after load reduction
- Error messages are meaningful

### 3. Endurance (Soak) Testing
**Purpose**: Identify memory leaks or resource exhaustion issues that manifest over time.

**Scenario**:
- Sustain typical load for extended period
- Monitor for gradual performance degradation
- Check for memory leaks and resource cleanup

**Configuration**:
- Duration: 2-4 hours (can extend to 24+ hours)
- Users: 200 concurrent users (constant)
- Target RPS: 100-200 requests/second (steady)
- Monitor: Memory, CPU, connections over time

**Success Criteria**:
- No performance degradation over time
- Memory usage stable (no leaks)
- No connection pool exhaustion
- Response times remain consistent

### 4. Spike Testing
**Purpose**: Assess the system's ability to handle sudden traffic spikes gracefully.

**Scenario**:
- Simulate sudden, extreme increases in user load
- Test auto-scaling and resource allocation
- Verify system stability during spikes

**Configuration**:
- Duration: 15 minutes
- Users: Sudden jumps (100 → 1000 → 100 → 2000 → 100)
- Spike Duration: 2 minutes each
- Recovery Period: 2 minutes between spikes

**Success Criteria**:
- System handles spikes without crashing
- Response times return to normal quickly
- No cascading failures
- Error rate acceptable during spike (< 5%)

## Running Performance Tests

### Prerequisites

1. **Start Infrastructure Services**:
   ```bash
   docker compose up -d postgres kafka redis
   ```

2. **Load Test Data**:
   ```bash
   # Run data seeding script if available
   ./gradlew bootRun --args='--seed-data'
   ```

3. **Start Application**:
   ```bash
   ./gradlew bootRun
   ```
   
   Or use Docker:
   ```bash
   docker compose up -d app
   ```

### Running Individual Tests

**Load Test**:
```bash
./gradlew runLoadTest
```

**Stress Test**:
```bash
./gradlew runStressTest
```

**Endurance Test**:
```bash
./gradlew runEnduranceTest
```

**Spike Test**:
```bash
./gradlew runSpikeTest
```

**All Tests**:
```bash
./gradlew runAllPerformanceTests
```

### Environment Configuration

Configure test parameters via environment variables:

```bash
export PERF_BASE_URL=http://localhost:8081
export PERF_TEST_USERS=500
export PERF_TEST_DURATION=1800  # 30 minutes in seconds
export PERF_RAMP_UP_TIME=300     # 5 minutes in seconds
```

## Analyzing Results

### Gatling Reports

After each test run, Gatling generates an HTML report in:
```
build/reports/gatling/<simulation-name>-<timestamp>/index.html
```

Key sections to review:
1. **Global Statistics**: Overall response time percentiles and throughput
2. **Response Time Distribution**: Histogram showing time distribution
3. **Requests per Second**: Throughput over time
4. **Response Time Percentiles**: p50, p75, p95, p99 trends
5. **Number of Requests/Responses**: Success vs. failure rates

### Prometheus Metrics

Access application metrics via:
```
http://localhost:8081/actuator/prometheus
```

Key metrics:
- `http_server_requests_seconds_*`: Request latency percentiles
- `jvm_memory_*`: Memory usage
- `jvm_threads_*`: Thread pool metrics
- `hikari_connections_*`: Database connection pool
- `system_cpu_usage`: CPU utilization

### Interpreting Results

**Good Performance Indicators**:
- Response times remain flat under increasing load
- Throughput scales linearly with users (up to a point)
- Error rate stays below 0.1% under normal load
- Resource usage stable (no spikes or trends up)
- 95th and 99th percentiles stay within acceptable ranges

**Warning Signs**:
- Response times increasing exponentially
- Throughput plateauing or decreasing
- Growing error rate
- Memory trending upward (potential leak)
- High CPU with low throughput (inefficient code)
- Connection pool exhaustion

## Optimization Strategies

If performance issues are identified:

### Application Layer
- Review slow endpoints (check logs for queries)
- Optimize database queries (add indexes, reduce N+1 queries)
- Implement caching for frequently accessed data
- Use async processing for non-critical operations
- Optimize JSON serialization

### Database Layer
- Add appropriate indexes
- Optimize slow queries (use EXPLAIN ANALYZE)
- Implement connection pooling tuning
- Consider read replicas for read-heavy workloads
- Implement query result caching

### Infrastructure Layer
- Increase thread pool sizes if needed
- Tune JVM heap size and GC settings
- Scale horizontally (add more instances)
- Implement load balancing
- Use CDN for static assets

### Architecture
- Implement circuit breakers for external services
- Add request queuing for spike protection
- Implement rate limiting at API gateway
- Use caching layers (Redis)
- Consider microservices for hot paths

## Continuous Performance Testing

### Integration into CI/CD

Add performance smoke tests to CI pipeline:
```yaml
performance-test:
  stage: test
  script:
    - docker compose up -d
    - ./gradlew runLoadTest
  artifacts:
    paths:
      - build/reports/gatling/
```

### Regular Performance Testing Schedule

- **Smoke Test**: After every deployment (5 min, 50 users)
- **Load Test**: Weekly (30 min, 500 users)
- **Stress Test**: Bi-weekly (20 min, to breaking point)
- **Endurance Test**: Monthly (4 hours, 200 users)

### Performance Budgets

Set performance budgets and fail builds if exceeded:
- p95 response time > 500ms: Warning
- p99 response time > 1000ms: Fail
- Error rate > 1%: Fail
- Throughput < 100 RPS at 200 users: Warning

## Troubleshooting Common Issues

### High Response Times
1. Check database query performance
2. Review application logs for slow operations
3. Check external API dependencies
4. Monitor thread pool saturation

### Memory Leaks
1. Take heap dumps before and after test
2. Analyze with tools like Eclipse Memory Analyzer
3. Check for unclosed resources (connections, streams)
4. Review caching configurations

### Connection Pool Exhaustion
1. Check connection pool size settings
2. Review connection leak detection
3. Ensure proper connection closing
4. Monitor connection usage patterns

### High CPU Usage
1. Take thread dumps during high CPU
2. Profile application with JProfiler or YourKit
3. Check for inefficient loops or algorithms
4. Review JSON processing overhead

## Additional Resources

- [Gatling Documentation](https://gatling.io/docs/current/)
- [Spring Boot Actuator Metrics](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Prometheus Monitoring](https://prometheus.io/docs/introduction/overview/)
- [Performance Testing Best Practices](https://gatling.io/load-testing-best-practices/)

## Contact

For questions or issues with performance testing:
- Review this documentation
- Check Gatling test logs in `build/reports/gatling/`
- Consult with the DevOps team for infrastructure concerns
- Reach out to the development team for application issues
