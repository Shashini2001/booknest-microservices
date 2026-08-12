# BookNest Client App

## Run locally
1. `npm install`
2. Make sure the API Gateway is running on http://localhost:8080 (or edit .env)
3. `npm start` -> opens http://localhost:3000

## What's already wired up
- src/api/axiosClient.js -> shared axios instance pointing at the Gateway, attaches JWT automatically
- src/api/bookApi.js, authApi.js -> one file per microservice
- src/context/AuthContext.jsx -> login state + token storage
- src/pages/Login.jsx -> working login form
- src/pages/BookCatalog.jsx -> working book grid with category filter, calls the real API

## Next steps for your team
1. Add pages/Register.jsx (copy Login.jsx pattern)
2. Add pages/BookDetail.jsx (fetch via getBookById)
3. Add src/api/readingApi.js, orderApi.js, deliveryApi.js (same pattern as bookApi.js)
4. Add pages/ReadingDashboard.jsx, Cart.jsx, Checkout.jsx, OrderTracking.jsx
5. Register each new page as a <Route> in App.jsx
