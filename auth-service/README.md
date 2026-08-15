# Auth Service (Student 1 - Gateway Lead)

## Run locally
1. Make sure MongoDB is running on localhost:27017
2. `mvn spring-boot:run`
3. Swagger UI: http://localhost:8081/swagger-ui.html
4. All endpoints require header: `X-API-KEY: auth-service-secret-key`

## Endpoints

### POST /auth/register
```json
{
  "fullName": "Nimal Perera",
  "email": "nimal@example.com",
  "password": "mypassword123"
}
```
Returns 201 + { token, userId, fullName, email, role }

### POST /auth/login
```json
{
  "email": "nimal@example.com",
  "password": "mypassword123"
}
```
Returns 200 + { token, userId, fullName, email, role }

## Test with Postman
1. POST http://localhost:8081/auth/register with the JSON body above
   Headers: Content-Type: application/json, X-API-KEY: auth-service-secret-key
2. Copy the "token" from the response
3. POST http://localhost:8081/auth/login to confirm login also works
4. This token is what the frontend stores and sends as
   "Authorization: Bearer <token>" on every other request
