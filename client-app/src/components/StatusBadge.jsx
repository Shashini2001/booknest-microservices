import React from "react";

export default function StatusBadge({ status }) {
  const cls = "status-" + (status || "placed").toLowerCase();
  return <span className={`status-badge ${cls}`}>{status}</span>;
}
