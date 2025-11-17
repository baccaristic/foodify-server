#!/bin/bash
set -e  # Exit immediately if a command exits with a non-zero status

# === CONFIG ===
PROJECT_DIR="/root/foodify/foodify-server"
cd "$PROJECT_DIR"

echo "🛑 Stopping and removing Docker containers..."
docker-compose down || true

echo "🚀 Pulling latest code from master..."
git fetch
git checkout master
git pull origin master

echo "🧹 Pruning old Docker resources..."
docker system prune -f

echo "🏗️ Building project with Gradle (skipping tests)..."
./gradlew build -x test

echo "🗑️  Cleaning up old JAR files..."
cd build/libs
rm -f server-0.0.1-SNAPSHOT-plain.jar || true
cd ../..

echo "✅ Build and cleanup complete!"
