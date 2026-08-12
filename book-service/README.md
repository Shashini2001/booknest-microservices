# Book Catalog Service (Student 2)

## Run locally
1. Make sure MongoDB is running on localhost:27017
2. `mvn spring-boot:run`
3. Swagger UI: http://localhost:8082/swagger-ui.html
4. All endpoints require header: `X-API-KEY: book-service-secret-key`

## Endpoints
- POST   /books
- GET    /books
- GET    /books/{id}
- PUT    /books/{id}
- DELETE /books/{id}
- GET    /books/category/{name}
- GET    /books/search?keyword=

## Use this as the template for the other 4 services
Copy this folder structure (model/repository/service/controller/filter/config)
and change: entity fields, port number, api.key value, mongodb database name.
