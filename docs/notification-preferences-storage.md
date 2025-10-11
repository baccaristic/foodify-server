# Notification Preferences Storage Strategy

The Notification Settings screen lets users opt in or out of order-status and marketing alerts. Persisting these preferences on the server (instead of only caching them in the mobile client) delivers better reliability and flexibility for the notification pipeline:

- **Multi-device consistency** – A user may log in from multiple devices (iOS, Android, web). Keeping the preferences in the database guarantees all clients see the same toggles and the notification service honors the latest choices across sessions.
- **Stateless push workflows** – Backend workers that send transactional or promotional notifications can query a single source of truth without relying on a specific device being online to report its cached state.
- **Auditability & compliance** – Storing preference changes (with timestamps/user IDs) enables audit trails and helps satisfy opt-in/opt-out regulations (e.g., GDPR, CAN-SPAM) that may require proof of consent withdrawal.
- **Server-driven experiments** – Marketing or ops teams can roll out campaigns or feature flags that respect preferences without requiring a mobile release to adjust local defaults.

To complement server persistence:

1. Cache the last-known values client-side so the UI stays responsive offline and during slow network conditions.
2. On toggle changes, optimistically update the UI, queue a sync to the API, and reconcile on success/failure.
3. When the app launches or the settings screen opens, refresh from the server to ensure the client-side cache stays aligned with the canonical store.

This hybrid approach keeps the database as the authoritative store while still giving users a responsive experience.
