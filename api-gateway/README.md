# API Gateway (Student 1)

Single entry point for the frontend. Routes /api/** requests to the
correct backend microservice by path prefix, and handles CORS so the
React app (localhost:3000) can call it.

## Port map (must match each service's application.properties)
- Auth Service      -> 8081  ->  /api/auth/**
- Book Service      -> 8087  ->  /api/books/**
- Reading Service   -> 8083  ->  /api/reading/**
- Order Service     -> 8084  ->  /api/orders/**
- Delivery Service  -> 8085  ->  /api/deliveries/**

If any teammate's service runs on a different port, update the matching
`uri:` value in src/main/resources/application.yml.

## Run locally
1. Start MongoDB + RabbitMQ
2. Start all 5 backend services first (each on its own port above)
3. `mvn spring-boot:run` -> gateway runs on port 8080
4. Test: http://localhost:8080/api/books  (should return the same JSON
   as calling http://localhost:8087/books directly)

## Client app
Point client-app/.env at this gateway:
```
REACT_APP_API_URL=http://localhost:8080/api
```
