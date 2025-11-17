#!/bin/bash
# This script creates all the directories and empty files for your Kustomize setup.
# Run it from your /k8s directory.

# Create all the subdirectories
# The -p flag ensures 'mkdir' doesn't error if the directories already exist
echo "Creating directories..."
mkdir -p app
mkdir -p postgres
mkdir -p redis
mkdir -p kafka

# Create all the empty files
echo "Creating root files..."
touch kustomization.yaml
touch namespace.yaml
touch secrets.yaml

echo "Creating app/ files..."
touch app/deployment.yaml
touch app/service.yaml
touch app/ingress.yaml
touch app/kustomization.yaml

echo "Creating postgres/ files..."
touch postgres/deployment.yaml
touch postgres/service.yaml
touch postgres/pvc.yaml
touch postgres/kustomization.yaml

echo "Creating redis/ files..."
touch redis/deployment.yaml
touch redis/service.yaml
touch redis/kustomization.yaml

echo "Creating kafka/ files..."
touch kafka/zookeeper-deployment.yaml
touch kafka/zookeeper-service.yaml
touch kafka/kafka-deployment.yaml
touch kafka/kafka-service.yaml
touch kafka/kustomization.yaml

echo "Done!"
ls -R
