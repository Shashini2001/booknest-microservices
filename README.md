# BookNest Microservices — Complete System
## Introduction
BookNest is a microservices-based online library and bookstore system that combines book purchasing with personal reading tracking. Users can browse books, make purchases, track deliveries, and maintain a personal reading dashboard with progress tracking, favorites, and reading statistics.


## Everything included
- auth-service, book-service, reading-progress-service, order-service, delivery-service
- api-gateway (routes /api/** to the right service, configurable via env vars)
- client-app (React, 11 pages, connected through the gateway)
- docker-compose.yml (runs the entire system with one command)

## System Architecture

<img width="1033" height="825" alt="Microservices-Based System Architecture drawio" src="https://github.com/user-attachments/assets/a220531e-44f4-42dc-a6bc-b13af38d0cb3" />

## Port map
| Service | Port |
|---|---|
| API Gateway | 8080 |
| Auth Service | 8081 |
| Book Catalog Service | 8087 |
| Reading Progress Service | 8083 |
| Order Service | 8084 |
| Delivery Service | 8085 |
| Client App | 3000 |
| MongoDB | 27017 |
| RabbitMQ | 5672 (AMQP), 15672 (management UI) |

## Fastest way to run everything
```
docker compose up --build
```
First run downloads base images and builds each service - can take several
minutes. After that, open http://localhost:3000

## Running without Docker (for development)
1. Start MongoDB (local install or `docker run -d -p 27017:27017 mongo`)
2. Start RabbitMQ (`docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management`)
3. In 6 separate terminals: `mvn spring-boot:run` inside each of
   auth-service, book-service, reading-progress-service, order-service,
   delivery-service, api-gateway (wait for "started" in each before
   moving to the next)
4. `cd client-app && npm install && npm start`



## Notes
- Client app always calls the Gateway (localhost:8080/api), never a
  service port directly.
- Each service has its own MongoDB database (authdb, bookdb, readingdb,
  orderdb, deliverydb) - all inside the same MongoDB container.
- Order placement publishes to RabbitMQ, Delivery Service consumes it
  automatically and creates a delivery record - no manual step needed.
