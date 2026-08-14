# auth-service

BookNest Auth & User Service. Runs on port 8081.

## Run it

```bash
docker run -d -p 27017:27017 mongo   # if Mongo isn't already running
mvn spring-boot:run
```

## Test it directly (before the gateway exists)

Swagger UI: http://localhost:8081/swagger-ui.html

All requests need header: `X-API-KEY: auth-service-secret-key`

1. `POST /auth/register`
   ```json
   { "fullName": "Ada Lovelace", "email": "ada@example.com", "passwordHash": "plaintext-password-here" }
   ```
   (the field is named `passwordHash` in the request body because it's reused as the raw
   password on the way in, then overwritten with the real hash before saving — see `AuthService.register`)

2. `POST /auth/login`
   ```json
   { "email": "ada@example.com", "password": "plaintext-password-here" }
   ```
   Returns `{ "token": "<jwt>" }`

3. `GET /users/{id}` — returns the user's profile (no password hash in the response)

4. `PUT /users/{id}/profile` — update `fullName` only

## Important: keep this in sync with api-gateway

`app.jwt.secret` in `application.properties` must be **byte-for-byte identical** to
`app.jwt.secret` in `api-gateway/src/main/resources/application.yml`. The gateway
re-derives the signing key from this string to verify tokens issued here — if they
drift, every authenticated request through the gateway will fail with 401 even
though this service works fine standalone.

Also generate a real random secret before anyone deploys this anywhere beyond
localhost — the placeholder in `application.properties` is not safe to keep.
