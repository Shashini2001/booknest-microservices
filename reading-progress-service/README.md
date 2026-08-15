# Reading Progress Service (Student 3)

## Run locally
1. MongoDB running on localhost:27017
2. `mvn spring-boot:run` -> port 8083
3. Swagger: http://localhost:8083/swagger-ui.html
4. Header required: `X-API-KEY: reading-service-secret-key`

## Endpoints
- POST   /reading                     - add a book to a user's list (wishlist/reading/completed)
- GET    /reading/{userId}            - all reading records for a user
- GET    /reading/{userId}/favorites  - user's favorite books
- GET    /reading/{userId}/stats      - dashboard chart data: totalBooksRead, currentlyReading, byMonth
- PUT    /reading/entry/{id}          - update status/progress/favorite
- DELETE /reading/entry/{id}

## Example - start reading a book
```json
POST /reading
{
  "userId": "user123",
  "bookId": "b1",
  "bookTitle": "The Alchemist",
  "status": "READING",
  "totalPages": 320,
  "pagesRead": 0
}
```

## Example - mark as completed (favorite it too)
```json
PUT /reading/entry/{id}
{
  "status": "COMPLETED",
  "pagesRead": 320,
  "isFavorite": true
}
```

## Example - stats response (feeds the dashboard chart)
```json
GET /reading/user123/stats
{
  "totalBooksRead": 12,
  "currentlyReading": 2,
  "byMonth": { "Jan": 2, "Feb": 1, "Mar": 3 }
}
```
