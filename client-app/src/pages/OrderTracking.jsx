import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getOrderById } from "../api/orderApi";
import { getDeliveryByOrderId } from "../api/deliveryApi";
import LoadingState from "../components/LoadingState";

const STEPS = ["PLACED", "CONFIRMED", "PACKED", "SHIPPED", "DELIVERED"];

export default function OrderTracking() {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [delivery, setDelivery] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = () => {
      getOrderById(orderId).then((res) => setOrder(res.data)).catch(() => {});
      getDeliveryByOrderId(orderId).then((res) => setDelivery(res.data)).catch(() => setDelivery(null));
    };
    load();
    const interval = setInterval(load, 5000); // poll for live status
    return () => clearInterval(interval);
  }, [orderId]);

  useEffect(() => { setLoading(false); }, [order]);

  if (loading && !order) return <LoadingState text="Loading order..." />;

  const currentIndex = order ? STEPS.indexOf(order.status) : -1;

  return (
    <div className="grid-2" style={{ maxWidth: 760 }}>
      <div className="card">
        <h2 style={{ fontSize: 18, marginBottom: 20 }}>Order Tracking</h2>
        <div className="timeline">
          {STEPS.map((step, i) => (
            <div key={step} className={`timeline-item${i <= currentIndex ? " done" : ""}`}>
              <div className="timeline-label">{step.charAt(0) + step.slice(1).toLowerCase()}</div>
              <div className="timeline-sub">
                {i === 0 && "Your order has been placed"}
                {i === 1 && "Seller confirmed your order"}
                {i === 2 && "Your books have been packed"}
                {i === 3 && "On the way to you"}
                {i === 4 && "Delivered to your address"}
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="card">
        <h3 className="section-title">Live courier location</h3>
        {delivery ? (
          <>
            <div className="map-placeholder">
              Rider {delivery.riderId} &middot; {delivery.currentLat.toFixed(3)}, {delivery.currentLng.toFixed(3)}
            </div>
            <div style={{ marginTop: 14, fontSize: 13, color: "var(--ink-soft)" }}>
              Status: <strong style={{ color: "var(--ink)" }}>{delivery.status}</strong><br />
              ETA: <strong style={{ color: "var(--ink)" }}>{delivery.eta}</strong>
            </div>
          </>
        ) : (
          <div className="map-placeholder">Waiting for courier assignment...</div>
        )}
      </div>
    </div>
  );
}
