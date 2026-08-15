import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Profile() {
  const { user, logoutUser } = useAuth();
  const navigate = useNavigate();

  const initials = user && user.fullName
    ? user.fullName.split(" ").map((p) => p[0]).slice(0, 2).join("").toUpperCase()
    : "?";

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  if (!user) return null;

  return (
    <div className="card" style={{ maxWidth: 340, textAlign: "center", padding: "36px 28px" }}>
      <div className="avatar" style={{ width: 72, height: 72, fontSize: 24, margin: "0 auto 16px" }}>
        {initials}
      </div>
      <h2 style={{ fontSize: 18, marginBottom: 2 }}>{user.fullName}</h2>
      <p style={{ color: "var(--ink-soft)", fontSize: 13, marginBottom: 24 }}>{user.email}</p>

      <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
        <button className="btn btn-primary">Edit Profile</button>
        <button className="btn btn-ghost" onClick={handleLogout}>Logout</button>
      </div>
    </div>
  );
}
