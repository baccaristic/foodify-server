# Migration Phase 5 – Outbox Telemetry & Operational Visibility

This phase hardens the order lifecycle outbox by adding production-grade observability.
The outbox processor now emits Micrometer metrics and OpenTelemetry observations so the
platform team can monitor dispatch throughput, failure rates, and disabled runs while the
migration progressively introduces downstream microservices.

## Highlights

- Persisted the lifecycle message type in the outbox table, keeping audit trails aligned
  with the published domain contract and removing the mismatched `eventType` reference.
- Instrumented the outbox processor with counters for dispatched, failed, and skipped
  runs, tagged by message type where applicable, enabling dashboards and alerts for the
  new messaging pipeline.
- Wrapped dispatch attempts in an OpenTelemetry observation to capture spans and error
  signals, creating an end-to-end trace that links order state changes with Kafka delivery.

## Next Steps

- Publish the new metrics (`app.orders.outbox.*`) to the observability stack and define
  SLOs/alerts for sustained failures or backlog growth.
- Extend the same instrumentation pattern to payments/logistics outboxes as they are
  introduced, ensuring cross-service sagas share consistent telemetry.
- Surface the recorded message types and failure traces in operational runbooks so the
  support team can triage downstream integration issues quickly.
