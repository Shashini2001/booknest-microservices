# BookNest Client App (Full Frontend)

11 pages, all wired to your 5 backend microservices through the Gateway.

## Pages included
- Login, Register
- Book Catalog (Home)
- Book Detail (add to cart / mark as reading / add to favorites)
- Reading Dashboard (chart + currently reading + completed)
- Cart
- Checkout
- Order History
- Order Tracking (status timeline + live courier panel, polls every 5s)
- Profile
- Admin Panel (book list with delete)

## Run locally
1. Make sure the API Gateway is running on http://localhost:8080
   (or edit .env: REACT_APP_API_URL)
2. npm install
3. npm start -> opens http://localhost:3000

## Design system
All styling lives in src/index.css as CSS variables (--paper, --primary, --gold, etc.)
Change the palette there to re-theme the whole app in one place.
Fonts: Lora (headings) + Inter (body), loaded from Google Fonts in public/index.html.

## Notes
- Cart state is kept in localStorage (see context/CartContext.jsx) so it survives refreshes.
- Auth token + user info also persist in localStorage (context/AuthContext.jsx).
- Order Tracking polls the Delivery Service every 5 seconds - swap for a
  WebSocket connection later for true real-time updates.
- Admin Panel currently only manages books; extend it to manage orders/users
  the same way if your assignment needs that.
