# Notification Preferences Storage Strategy

The Notification Settings screen presents three toggles that mirror the design shared by product:

1. **Order updates** – a master switch that controls all transactional push notifications related to order progress.
2. **Marketing via push** – opt in/out of promotional pushes.
3. **Marketing via email** – opt in/out of promotional emails.

Persisting these preferences on the server (instead of caching them only on the mobile client) delivers better reliability and flexibility for the notification pipeline:

- **Multi-device consistency** – users frequently sign in from multiple devices. Storing the toggles in the database ensures every session reflects the same state and transactional senders read the latest choice.
- **Stateless push workflows** – background workers that deliver order updates or marketing campaigns can check one authoritative store before contacting Firebase or the email provider.
- **Auditability & compliance** – the persisted timestamps give us an audit trail that helps satisfy opt-in/opt-out regulations (GDPR, CAN-SPAM, etc.).
- **Server-driven control** – growth and support teams can launch campaigns or temporarily force-enable order updates without shipping a mobile release to patch local defaults.

## API contract

```
GET  /api/notifications/preferences
PUT  /api/notifications/preferences
POST /api/notifications/preferences/enable-all
```

- `GET` returns all three toggles with their current `enabled` state and last update timestamp (if persisted).
- `PUT` accepts a list of `{ type, enabled }` updates so the client can submit individual toggle changes.
- `POST .../enable-all` is used by the "Enable all" button in the UI to flip every toggle on in a single call.

## Client-side considerations

1. Cache the last-known values locally so the settings screen opens instantly, then reconcile with the API in the background.
2. Optimistically update the UI when the user flips a toggle and roll back if the API call fails.
3. Refresh from the server when the screen appears so the local cache stays aligned with the canonical store.

This hybrid approach keeps the database as the authoritative store while still giving users a responsive experience that matches the mockups.
