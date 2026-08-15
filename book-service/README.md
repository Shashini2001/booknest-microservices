# Book Catalog Service (Student 2)

## Run locally
1. MongoDB running on localhost:27017
2. `mvn spring-boot:run` -> port 8087
3. Swagger: http://localhost:8087/swagger-ui.html
4. Header required: `X-API-KEY: book-service-secret-key`

## Endpoints
- POST   /books
- GET    /books
- GET    /books/{id}
- PUT    /books/{id}
- DELETE /books/{id}
- GET    /books/category/{name}
- GET    /books/search?keyword=
