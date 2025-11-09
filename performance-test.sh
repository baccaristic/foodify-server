#!/bin/bash

# Performance Test Runner Script for Foodify Server
# This script provides easy commands to run different performance test scenarios

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default configuration
BASE_URL="${PERF_BASE_URL:-http://localhost:8081}"
TEST_USER_EMAIL="${PERF_TEST_USER_EMAIL:-test@foodify.com}"
TEST_USER_PASSWORD="${PERF_TEST_USER_PASSWORD:-password123}"

# Function to print colored messages
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if server is running
check_server() {
    print_info "Checking if server is running at $BASE_URL..."
    if curl -s -f "$BASE_URL/actuator/health" > /dev/null; then
        print_success "Server is running and healthy"
        return 0
    else
        print_error "Server is not accessible at $BASE_URL"
        print_info "Please start the server with: ./gradlew bootRun"
        return 1
    fi
}

# Function to display usage
usage() {
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Commands:"
    echo "  load        Run load test (30 min, 200 users)"
    echo "  stress      Run stress test (20 min, progressive load)"
    echo "  endurance   Run endurance test (2 hours, sustained load)"
    echo "  spike       Run spike test (15 min, sudden spikes)"
    echo "  all         Run all performance tests"
    echo "  smoke       Run quick smoke test (5 min, 50 users)"
    echo ""
    echo "Options:"
    echo "  --url URL           Target server URL (default: $BASE_URL)"
    echo "  --users NUM         Number of users for load test"
    echo "  --duration SEC      Test duration in seconds"
    echo "  --skip-check        Skip server health check"
    echo ""
    echo "Examples:"
    echo "  $0 load"
    echo "  $0 load --users 500 --duration 3600"
    echo "  $0 stress --url http://staging.foodify.com:8081"
    echo "  $0 smoke"
    echo ""
}

# Function to run load test
run_load_test() {
    print_info "Running Load Test..."
    print_info "Configuration:"
    print_info "  - Base URL: $BASE_URL"
    print_info "  - Users: ${USERS:-200}"
    print_info "  - Duration: ${DURATION:-1800} seconds"
    
    ./gradlew runLoadTest \
        -Dperf.baseUrl="$BASE_URL" \
        -Dperf.testUserEmail="$TEST_USER_EMAIL" \
        -Dperf.testUserPassword="$TEST_USER_PASSWORD" \
        ${USERS:+-Dperf.normalUsers=$USERS} \
        ${DURATION:+-Dperf.duration=$DURATION}
    
    print_success "Load test completed!"
    show_latest_report
}

# Function to run stress test
run_stress_test() {
    print_info "Running Stress Test..."
    print_info "Configuration:"
    print_info "  - Base URL: $BASE_URL"
    print_info "  - Max Users: ${STRESS_USERS:-2000}"
    
    ./gradlew runStressTest \
        -Dperf.baseUrl="$BASE_URL" \
        -Dperf.testUserEmail="$TEST_USER_EMAIL" \
        -Dperf.testUserPassword="$TEST_USER_PASSWORD" \
        ${STRESS_USERS:+-Dperf.stressUsers=$STRESS_USERS}
    
    print_success "Stress test completed!"
    show_latest_report
}

# Function to run endurance test
run_endurance_test() {
    print_info "Running Endurance Test..."
    print_info "Configuration:"
    print_info "  - Base URL: $BASE_URL"
    print_info "  - Duration: ${ENDURANCE_HOURS:-2} hours"
    
    ./gradlew runEnduranceTest \
        -Dperf.baseUrl="$BASE_URL" \
        -Dperf.testUserEmail="$TEST_USER_EMAIL" \
        -Dperf.testUserPassword="$TEST_USER_PASSWORD" \
        ${ENDURANCE_HOURS:+-Dperf.endurance.hours=$ENDURANCE_HOURS}
    
    print_success "Endurance test completed!"
    show_latest_report
}

# Function to run spike test
run_spike_test() {
    print_info "Running Spike Test..."
    print_info "Configuration:"
    print_info "  - Base URL: $BASE_URL"
    
    ./gradlew runSpikeTest \
        -Dperf.baseUrl="$BASE_URL" \
        -Dperf.testUserEmail="$TEST_USER_EMAIL" \
        -Dperf.testUserPassword="$TEST_USER_PASSWORD"
    
    print_success "Spike test completed!"
    show_latest_report
}

# Function to run smoke test (quick validation)
run_smoke_test() {
    print_info "Running Smoke Test (Quick Performance Check)..."
    print_info "Configuration:"
    print_info "  - Base URL: $BASE_URL"
    print_info "  - Users: 50"
    print_info "  - Duration: 300 seconds (5 minutes)"
    
    ./gradlew runLoadTest \
        -Dperf.baseUrl="$BASE_URL" \
        -Dperf.testUserEmail="$TEST_USER_EMAIL" \
        -Dperf.testUserPassword="$TEST_USER_PASSWORD" \
        -Dperf.normalUsers=50 \
        -Dperf.duration=300 \
        -Dperf.rampUp=60
    
    print_success "Smoke test completed!"
    show_latest_report
}

# Function to run all tests
run_all_tests() {
    print_info "Running ALL Performance Tests (this will take several hours)..."
    
    run_smoke_test
    print_info "Waiting 2 minutes before next test..."
    sleep 120
    
    run_load_test
    print_info "Waiting 2 minutes before next test..."
    sleep 120
    
    run_stress_test
    print_info "Waiting 2 minutes before next test..."
    sleep 120
    
    run_spike_test
    
    print_success "All performance tests completed!"
    print_info "Review reports in: build/reports/gatling/"
}

# Function to show latest report location
show_latest_report() {
    if [ -d "build/reports/gatling" ]; then
        LATEST_REPORT=$(ls -t build/reports/gatling/ | head -1)
        if [ -n "$LATEST_REPORT" ]; then
            REPORT_PATH="build/reports/gatling/$LATEST_REPORT/index.html"
            print_info "Report available at: $REPORT_PATH"
            
            # Try to open report in browser (works on macOS and Linux)
            if command -v open &> /dev/null; then
                print_info "Opening report in browser..."
                open "$REPORT_PATH"
            elif command -v xdg-open &> /dev/null; then
                print_info "Opening report in browser..."
                xdg-open "$REPORT_PATH"
            fi
        fi
    fi
}

# Parse command line arguments
COMMAND=""
SKIP_CHECK=false

while [[ $# -gt 0 ]]; do
    case $1 in
        load|stress|endurance|spike|all|smoke)
            COMMAND=$1
            shift
            ;;
        --url)
            BASE_URL="$2"
            shift 2
            ;;
        --users)
            USERS="$2"
            shift 2
            ;;
        --duration)
            DURATION="$2"
            shift 2
            ;;
        --skip-check)
            SKIP_CHECK=true
            shift
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

# Main execution
if [ -z "$COMMAND" ]; then
    print_error "No command specified"
    usage
    exit 1
fi

# Check if server is running (unless skipped)
if [ "$SKIP_CHECK" = false ]; then
    check_server || exit 1
fi

# Execute the requested command
case $COMMAND in
    load)
        run_load_test
        ;;
    stress)
        run_stress_test
        ;;
    endurance)
        run_endurance_test
        ;;
    spike)
        run_spike_test
        ;;
    smoke)
        run_smoke_test
        ;;
    all)
        run_all_tests
        ;;
    *)
        print_error "Unknown command: $COMMAND"
        usage
        exit 1
        ;;
esac

exit 0
