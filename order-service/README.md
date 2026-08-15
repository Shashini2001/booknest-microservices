# Order Service (Student 4)

## Prerequisites
1. MongoDB running on localhost:27017
2. RabbitMQ running: `docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management`

## Run locally
`mvn spring-boot:run` -> port 8084
Swagger: http://localhost:8084/swagger-ui.html
Header required: `X-API-KEY: order-service-secret-key`

## Endpoints
- POST   /orders/checkout
- GET    /orders                 (optional ?userId=)
- GET    /orders/{id}
- PUT    /orders/{id}/status     body: {"status": "SHIPPED"}
- DELETE /orders/{id}            (cancels the order)

## Test checkout
```json
POST /orders/checkout
{
  "userId": "user123",
  "deliveryAddress": "123 Main St, Colombo",
  "items": [
    { "bookId": "b1", "title": "The Alchemist", "quantity": 1, "unitPrice": 1200 }
  ]
}
```
On success, this also publishes an OrderPlaced event to RabbitMQ queue "order.placed".
If Delivery Service is running, check its /deliveries endpoint - a delivery
record should appear automatically within a second or two.
