import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getOrders } from "../api/orderApi";
import { useAuth } from "../context/AuthContext";
import StatusBadge from "../components/StatusBadge";
import LoadingState from "../components/LoadingState";
import EmptyState from "../components/EmptyState";

export default function OrderHistory() {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) return;
    getOrders(user.id).then((res) => setOrders(res.data)).finally(() => setLoading(false));
  }, [user]);

  if (loading) return <LoadingState text="Loading your orders..." />;
  if (orders.length === 0) return <EmptyState title="No orders yet" subtitle="Your placed orders will show up here." />;

  return (
    <div>
      <h1 style={{ fontSize: 22, marginBottom: 18 }}>Order History</h1>
      <div className="card">
        {orders.map((o) => (
          <Link to={`/orders/${o.id}/track`} key={o.id} className="reading-row" style={{ cursor: "pointer" }}>
            <div className="reading-row-info">
              <div className="reading-row-title">Order #{o.id.slice(-6).toUpperCase()}</div>
              <div className="reading-row-sub">
                {new Date(o.createdAt).toLocaleDateString()} &middot; Rs. {o.totalAmount}
              </div>
            </div>
            <StatusBadge status={o.status} />
          </Link>
        ))}
      </div>
    </div>
  );
}
