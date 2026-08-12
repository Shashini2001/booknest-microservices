# BookNest Starter Kit

## What's inside
- /book-service  - fully working Spring Boot microservice (Book Catalog), ready to run
- /client-app    - React frontend, already connected to book-service through a Gateway URL

## How to run this starter right now
1. Start MongoDB locally (or `docker run -d -p 27017:27017 mongo`)
2. cd book-service && mvn spring-boot:run   -> runs on http://localhost:8082
3. Test directly: http://localhost:8082/swagger-ui.html (header X-API-KEY: book-service-secret-key)
4. For the frontend to work fully you still need the API Gateway on port 8080
   routing /api/books/** -> localhost:8082 (see the implementation guide, Part C)
   As a quick local test without the gateway, you can temporarily change
   client-app/.env to REACT_APP_API_URL=http://localhost:8082 and remove the
   X-API-KEY requirement, just to see the UI working end-to-end.
5. cd client-app && npm install && npm start -> http://localhost:3000

## Use book-service as the copy-paste template
For Auth, Reading Progress, Order, and Delivery services: copy this folder,
rename the package, change the entity fields, change the port and api.key
in application.properties, and follow the same model/repository/service/
controller/filter/config pattern.
