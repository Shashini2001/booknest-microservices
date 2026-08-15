import React from "react";

export default function EmptyState({ title, subtitle }) {
  return (
    <div className="empty-state">
      <h3 style={{ fontSize: 16, marginBottom: 6 }}>{title}</h3>
      {subtitle && <p style={{ fontSize: 13 }}>{subtitle}</p>}
    </div>
  );
}
