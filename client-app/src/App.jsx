import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import { SearchProvider } from "./context/SearchContext";
import Layout from "./components/Layout";
import ProtectedRoute from "./components/ProtectedRoute";

import Login from "./pages/Login";
import Register from "./pages/Register";
import BookCatalog from "./pages/BookCatalog";
import BookDetail from "./pages/BookDetail";
import ReadingDashboard from "./pages/ReadingDashboard";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import OrderHistory from "./pages/OrderHistory";
import OrderTracking from "./pages/OrderTracking";
import Profile from "./pages/Profile";
import AdminPanel from "./pages/AdminPanel";

export default function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <SearchProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>
              <Route path="/" element={<BookCatalog />} />
              <Route path="/books/:id" element={<BookDetail />} />
              <Route path="/dashboard" element={<ReadingDashboard />} />
              <Route path="/cart" element={<Cart />} />
              <Route path="/checkout" element={<Checkout />} />
              <Route path="/orders" element={<OrderHistory />} />
              <Route path="/orders/:orderId/track" element={<OrderTracking />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/admin" element={<AdminPanel />} />
            </Route>
          </Routes>
        </BrowserRouter>
        </SearchProvider>
      </CartProvider>
    </AuthProvider>
  );
}
