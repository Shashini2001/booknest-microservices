# Delivery & Tracking Service (Student 5)

## Prerequisites
1. MongoDB running on localhost:27017
2. RabbitMQ running: `docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management`

## Run locally
`mvn spring-boot:run` -> port 8085
Swagger: http://localhost:8085/swagger-ui.html
Header required: `X-API-KEY: delivery-service-secret-key`

## Endpoints
- POST   /deliveries              (manual create, for testing)
- GET    /deliveries              (list all)
- GET    /deliveries/{orderId}    (live status/location/ETA)
- PUT    /deliveries/{orderId}    body: {"status": "ON_THE_WAY", "currentLat": 6.93, "currentLng": 79.85}
- DELETE /deliveries/{orderId}

## How the automatic flow works
1. Order Service publishes an "OrderPlaced" event to RabbitMQ when checkout happens
2. This service's OrderPlacedListener consumes it automatically
3. A Delivery record is created with status ASSIGNED and a random rider ID
4. GET /deliveries/{orderId} immediately shows the new delivery

## Test the full flow
1. Start MongoDB + RabbitMQ + this service + order-service
2. POST to order-service's /orders/checkout
3. Copy the returned order "id"
4. GET http://localhost:8085/deliveries/{that id} on this service
   -> should show status: ASSIGNED automatically, with no manual step needed
