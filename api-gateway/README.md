# api-gateway

BookNest API Gateway. Runs on port 8080. The React client only ever talks to this port.

## Critical setup step before running

`app.jwt.secret` in `src/main/resources/application.yml` must be **byte-for-byte
identical** to `app.jwt.secret` in `auth-service/src/main/resources/application.properties`.
The gateway re-derives the HMAC signing key from this string to verify tokens issued
by auth-service. If they don't match exactly, every authenticated request will fail
with 401 even though both services are individually healthy.

## Run it

Start auth-service (and any other services you're routing to) first, then:

```bash
mvn spring-boot:run
```

Runs on http://localhost:8080

## Test it

1. **Public route (no token needed):**
   ```
   POST http://localhost:8080/api/auth/login
   Header: X-API-KEY: auth-service-secret-key
   Body: { "email": "ada@example.com", "password": "test1234" }
   ```
   Should succeed and return a token — same as calling :8081 directly, just
   proxied through the gateway. Confirms routing + StripPrefix work.

2. **Protected route without a token:**
   ```
   GET http://localhost:8080/api/users/{id}
   ```
   No Authorization header. Should return `401`.

3. **Protected route with a valid token:**
   ```
   GET http://localhost:8080/api/users/{id}
   Header: Authorization: Bearer <token from step 1>
   Header: X-API-KEY: auth-service-secret-key
   ```
   Should return the user profile. Note both headers are required — the gateway's
   JwtAuthFilter checks the Bearer token, then forwards the request to auth-service,
   which independently checks its own X-API-KEY. Two separate layers, both must pass.

4. **Rate limit:**
   Send 21+ requests to any route within a minute (a simple loop or Postman Runner
   works). The 21st should return `429 Too Many Requests`.

## Adding routes for teammates' services

The routes for book-service (8082), reading-service (8083), order-service (8084),
and delivery-service (8085) are already stubbed in `application.yml`. As each
teammate's service comes online, no changes should be needed here as long as they
stick to their assigned port — just restart the gateway to pick up route changes.
