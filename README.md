# BookNest — Online Library & Bookstore with Reading Tracker

**Module:** Service Oriented Computing — Microservices-Based System Architecture Project
**Project Supervisor:** Mr. Anjana Chanakya Bandara Abeykoon

BookNest is a distributed, event-driven microservices system for browsing, buying, reading, and tracking books. Five independently owned Spring Boot services sit behind a single API Gateway and are consumed by one shared React client, with Order and Delivery talking to each other asynchronously over RabbitMQ instead of calling each other directly.

## Group Members

| Student ID | Name | Microservice(s) Owned |
|---|---|---|
| ITBIN-2313-0015 | Hasniya Banu | Auth & User Service, Order Service |
| ITBIN-2313-0087 | H.T.D. Rajapaksha | Reading Progress Service |
| ITBIN-2313-0095 | R.A. Shashini Ruwanthika | Book Catalog Service, Delivery & Tracking Service |

## Architecture
<img width="1033" height="825" alt="Microservices-Based System Architecture drawio" src="https://github.com/user-attachments/assets/a220531e-44f4-42dc-a6bc-b13af38d0cb3" />

The client and every microservice talk to each other **only** through the API Gateway — nothing calls another service's port directly except the Gateway itself. The one exception is Order ↔ Delivery, which are deliberately *not* wired together with a direct call: they exchange asynchronous events over RabbitMQ instead, so placing an order never blocks on the delivery record being created.

Supporting infrastructure: **MongoDB** (`:27017`, one instance / five logical databases), **RabbitMQ** (`:5672`, management UI on `:15672`), and **Mongo Express** (`:8090`) for browsing the databases during development.

### Request lifecycle — placing an order

1. Client logs in → Auth & User Service verifies credentials against `authdb` and returns a signed JWT.
2. Client browses `/api/books` → Book Catalog Service serves the catalog from `bookdb`.
3. Client calls `POST /api/orders/checkout` with a `Bearer` token → Gateway forwards to Order Service (`:8084`), auto-attaching `X-API-KEY: order-service-secret-key`.
4. Order Service persists the order in `orderdb` and publishes an `OrderPlacedEvent` to RabbitMQ, then returns immediately — it does not wait on delivery creation.
5. Delivery & Tracking Service consumes `OrderPlacedEvent` and auto-creates the matching delivery record in `deliverydb`, with no separate API call from the client.
6. When the delivery status changes, Delivery publishes `DeliveryStatusUpdatedEvent`; Order Service consumes it to keep the order's own status field in sync.

## Prerequisites

- Docker Desktop (with Docker Compose)
- Ports `3000, 8080–8087, 8090, 27017, 5672, 15672` free on your machine

## Running the System

Clone the repository and, from the project root:

```bash
docker compose up -d
```

This builds and starts all ten containers: the five microservices, the API Gateway, the React client, MongoDB, RabbitMQ, and Mongo Express.

Check everything is up:

```bash
docker ps
```

Once all containers show `Up`, open the client:

```
http://localhost:3000
```

To stop everything:

```bash
docker compose down
```

## Service Directory

| Service | Port | Database | Swagger UI |
|---|---|---|---|
| API Gateway | 8080 | — | — |
| Auth & User Service | 8081 | `authdb` | http://localhost:8081/swagger-ui.html |
| Order Service | 8084 | `orderdb` | http://localhost:8084/swagger-ui.html |
| Delivery & Tracking Service | 8085 | `deliverydb` | http://localhost:8085/swagger-ui.html |
| Reading Progress Service | 8086 | `readingdb` | http://localhost:8086/swagger-ui.html |
| Book Catalog Service | 8087 | `bookdb` | http://localhost:8087/swagger-ui.html |
| Mongo Express (DB browser) | 8090 | all | — |

All application traffic should go through the Gateway (`http://localhost:8080/api/...`) — the individual service ports above are exposed for direct Swagger access and debugging only.

## Authentication

1. `POST http://localhost:8080/api/auth/register` — create an account
2. `POST http://localhost:8080/api/auth/login` — returns a signed JWT
3. Send the token on every subsequent request:

```
Authorization: Bearer <token>
```

## API Keys (per microservice)

Each microservice independently verifies an `X-API-KEY` header via its own `ApiKeyFilter`, in addition to the Gateway-level JWT check. **The Gateway attaches these automatically** for any request routed through it — they're listed here for direct-to-service testing (e.g. via Swagger) or Postman collections that bypass the Gateway.

| Service | Header | Key |
|---|---|---|
| Auth & User Service | `X-API-KEY` | `auth-service-secret-key` |
| Book Catalog Service | `X-API-KEY` | `book-service-secret-key` |
| Reading Progress Service | `X-API-KEY` | `reading-service-secret-key` |
| Order Service | `X-API-KEY` | `order-service-secret-key` |
| Delivery & Tracking Service | `X-API-KEY` | `delivery-service-secret-key` |

## How Authentication Actually Works

The Auth & User Service is the only place a JWT is minted (`JwtService`):

- **Subject** — the user's MongoDB `_id`
- **Claims** — `email`, `role`
- **Signing** — HMAC-SHA, keyed by `app.jwt.secret`
- **Expiry** — configurable via `app.jwt.expiration-ms`

Every other service trusts a valid signature from this one issuer rather than re-implementing login — the same centralised-issuer, bearer-token pattern OAuth2 Resource Servers use, backed here by a shared-secret JWT.

## Data Models

<details>
<summary><b>User</b> (authdb)</summary>

`id, fullName, email, passwordHash (BCrypt), role, createdAt`
</details>

<details>
<summary><b>Book</b> (bookdb)</summary>

`id, title, author, category, description, coverUrl, price, stock, rating, pdfUrl`
</details>

<details>
<summary><b>ReadingProgress</b> (readingdb)</summary>

`id, userId, bookId, bookTitle, coverUrl, status (WISHLIST | READING | COMPLETED), totalPages, pagesRead, favorite`
</details>

<details>
<summary><b>Order</b> (orderdb)</summary>

`id, userId, items[] (bookId, title, quantity, unitPrice), totalAmount, status, deliveryAddress, createdAt`
</details>

<details>
<summary><b>Delivery</b> (deliverydb)</summary>

`id, orderId, status (PENDING | SHIPPED | DELIVERED), courier, deliveryAddress, updatedAt`
</details>

## Known Issues & Fixes Applied

| Issue | Cause | Fix |
|---|---|---|
| PATCH requests (page progress, favourites) blocked from the browser | Gateway's CORS `allowedMethods` didn't include `PATCH` | Added `PATCH` to the Gateway's global CORS config |
| Services failing to build in Docker | `pom.xml` targeted Java 25, ahead of the build image's JDK | Standardised all services on Java 17 |
| `reading-progress-service` unreachable on its mapped port | Dockerfile `EXPOSE` didn't match `server.port` | Aligned `EXPOSE` with `application.properties` |
| Order/Delivery Postman calls returning 404/405 | Assumed REST paths didn't match the real controllers | Corrected to `POST /orders/checkout` and `PUT /deliveries/{orderId}` |
| Swagger UI returning 500 on some services | `springdoc-openapi 2.5.0` incompatible with Spring Boot 3.5.5 | Upgrade `springdoc-openapi` to a 3.4/3.5-compatible release |

## Test Credentials

```json
{
  "email": "postmantest@gmail.com",
  "password": "test123"
}
```

## Endpoint Summary

### Auth & User Service — `/api/auth`
```
POST /auth/register
POST /auth/login
```

### Book Catalog Service — `/api/books`
```
GET    /books
GET    /books/{id}
POST   /books
PUT    /books/{id}
DELETE /books/{id}
GET    /books/category/{name}
GET    /books/search?keyword=
POST   /books/{id}/upload-pdf
GET    /books/pdf/{filename}
```

### Reading Progress Service — `/api/reading`
```
GET    /reading/{userId}
GET    /reading/{userId}/favorites
GET    /reading/{userId}/stats
POST   /reading
PUT    /reading/entry/{id}
PATCH  /reading/entry/{id}/pages
PATCH  /reading/entry/{id}/favorite
DELETE /reading/entry/{id}
```

### Order Service — `/api/orders`
```
POST   /orders/checkout
GET    /orders
GET    /orders/{id}
PUT    /orders/{id}/status
DELETE /orders/{id}
```

### Delivery & Tracking Service — `/api/deliveries`
```
POST   /deliveries
GET    /deliveries
GET    /deliveries/{orderId}
PUT    /deliveries/{orderId}
DELETE /deliveries/{orderId}
```

## Testing

A full **Postman collection** (`BookNest_API.postman_collection.json`) and matching **environment file** (`BookNest_Local.postman_environment.json`) are included in `/postman`, covering all five services with the JWT token auto-saved on login and both API-key variables pre-filled.

## Repository Structure

```
booknest-microservices/
├── auth-service/
├── book-service/
├── reading-progress-service/
├── order-service/
├── delivery-service/
├── api-gateway/
├── client-app/
├── postman/
├── docker-compose.yml
└── README.md
```

## Tech Stack

- **Backend:** Spring Boot (Java), Spring Cloud Gateway, Spring Security, Spring Data MongoDB, Spring AMQP (RabbitMQ), springdoc-openapi
- **Frontend:** React, React Router, Axios, Recharts
- **Data & Messaging:** MongoDB, RabbitMQ
- **Infrastructure:** Docker, Docker Compose, Mongo Express

## Future Work

- Replace the shared-secret JWT with a full OAuth2 Authorization Server (Spring Authorization Server / Keycloak) for proper token issuance, refresh, and revocation.
- Add rate limiting at the Gateway (Spring Cloud Gateway's `RequestRateLimiter`, backed by Redis) per client/IP.
- Add a circuit breaker (Resilience4j) at the Gateway so a slow/failing service degrades gracefully instead of blocking the request chain.
- Extend the RabbitMQ event model beyond Order↔Delivery — e.g. a stock-changed event so the catalog stays in sync without polling.
- Add centralised logging/tracing (ELK or Grafana + Loki with correlation ids) to follow one request across all five services.




