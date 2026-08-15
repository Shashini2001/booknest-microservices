import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { checkout } from "../api/orderApi";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

export default function Checkout() {
  const { items, subtotal, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [fullName, setFullName] = useState(user ? user.fullName : "");
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [error, setError] = useState("");
  const [placing, setPlacing] = useState(false);

  const handlePlaceOrder = async (e) => {
    e.preventDefault();
    setError("");
    setPlacing(true);
    try {
      const res = await checkout({
        userId: user.id,
        deliveryAddress: `${address}, ${city}`,
        items: items.map((i) => ({ bookId: i.bookId, title: i.title, quantity: i.quantity, unitPrice: i.unitPrice })),
      });
      clearCart();
      navigate(`/orders/${res.data.id}/track`);
    } catch (err) {
      setError("Could not place your order. Please check that the Order Service is running.");
    } finally {
      setPlacing(false);
    }
  };

  return (
    <div className="grid-2" style={{ maxWidth: 760 }}>
      <div className="card">
        <h2 style={{ fontSize: 18, marginBottom: 18 }}>Checkout Page</h2>
        <form onSubmit={handlePlaceOrder}>
          <div className="field-group">
            <label className="field-label">Shipping address</label>
            <input placeholder="Full Name" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          </div>
          <div className="field-group">
            <input placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} required />
          </div>
          <div className="field-group">
            <input placeholder="City" value={city} onChange={(e) => setCity(e.target.value)} required />
          </div>
          {error && <p className="error-text">{error}</p>}
          <button className="btn btn-primary btn-block" type="submit" disabled={placing || items.length === 0}>
            {placing ? "Placing order..." : "Place Order"}
          </button>
        </form>
      </div>

      <div className="card">
        <h3 className="section-title">Order Summary</h3>
        {items.map((i) => (
          <div key={i.bookId} className="summary-row"><span>{i.title} x{i.quantity}</span><span>Rs. {(i.unitPrice * i.quantity).toFixed(2)}</span></div>
        ))}
        <div className="summary-total"><span>Total</span><span>Rs. {subtotal.toFixed(2)}</span></div>
      </div>
    </div>
  );
}
