import React from "react";
import { useNavigate } from "react-router-dom";
import { Minus, Plus, X } from "lucide-react";
import { useCart } from "../context/CartContext";
import EmptyState from "../components/EmptyState";

export default function Cart() {
  const { items, updateQuantity, removeFromCart, subtotal } = useCart();
  const navigate = useNavigate();
  const tax = 0;
  const total = subtotal + tax;

  if (items.length === 0) {
    return <EmptyState title="Your cart is empty" subtitle="Browse the catalog and add a few books." />;
  }

  return (
    <div className="cart-layout">
      <div className="card">
        <h2 style={{ fontSize: 18, marginBottom: 8 }}>Your Cart</h2>
        {items.map((item) => (
          <div className="cart-item" key={item.bookId}>
            <img className="cart-item-cover" src={item.coverUrl || "https://placehold.co/80x112/ECE5D6/6B6255?text=Book"} alt="" />
            <div className="cart-item-info">
              <div className="cart-item-title">{item.title}</div>
              <div className="cart-item-price">Rs. {item.unitPrice}</div>
            </div>
            <div className="qty-control">
              <button className="qty-btn" onClick={() => updateQuantity(item.bookId, item.quantity - 1)}><Minus size={13} /></button>
              <span>{item.quantity}</span>
              <button className="qty-btn" onClick={() => updateQuantity(item.bookId, item.quantity + 1)}><Plus size={13} /></button>
            </div>
            <button className="qty-btn" onClick={() => removeFromCart(item.bookId)}><X size={13} /></button>
          </div>
        ))}
      </div>

      <div className="card">
        <h3 className="section-title">Order Summary</h3>
        <div className="summary-row"><span>Subtotal</span><span>Rs. {subtotal.toFixed(2)}</span></div>
        <div className="summary-row"><span>Tax / delivery</span><span>Rs. {tax.toFixed(2)}</span></div>
        <div className="summary-total"><span>Total</span><span>Rs. {total.toFixed(2)}</span></div>
        <button className="btn btn-primary btn-block" style={{ marginTop: 18 }} onClick={() => navigate("/checkout")}>
          Proceed to Checkout
        </button>
      </div>
    </div>
  );
}
