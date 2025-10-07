# Kubernetes Deployment Snippets

The manifests in this directory illustrate how to enable the new platform integrations
introduced during the monolith-to-microservices migration.

- `foodify-platform.yaml` deploys the application with the discovery, messaging,
  caching, and tracing flags turned on. It expects supporting services such as Eureka,
  Kafka, Redis, and the OTLP collector to be reachable within the cluster.
- Database credentials are pulled from the `foodify-database` secret to avoid embedding
  sensitive values directly in the manifest.

Apply the manifests with `kubectl apply -f foodify-platform.yaml` after provisioning the
supporting infrastructure (Config Server, Eureka, Kafka, Redis, OTLP collector, etc.).
Customize the replica counts, resource requests, and secret references to match your
cluster conventions before promoting to higher environments.
