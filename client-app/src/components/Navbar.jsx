import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logoutUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  return (
    <nav style={styles.nav}>
      <Link to="/" style={styles.logo}>📚 BookNest</Link>
      <div style={styles.links}>
        <Link to="/" style={styles.link}>Browse</Link>
        <Link to="/dashboard" style={styles.link}>Dashboard</Link>
        <Link to="/cart" style={styles.link}>Cart</Link>
        {user ? (
          <button onClick={handleLogout} style={styles.button}>Logout</button>
        ) : (
          <Link to="/login" style={styles.link}>Login</Link>
        )}
      </div>
    </nav>
  );
}

const styles = {
  nav: {
    display: "flex", justifyContent: "space-between", alignItems: "center",
    padding: "14px 32px", backgroundColor: "#1F3864", color: "white",
  },
  logo: { color: "white", fontWeight: "bold", fontSize: "20px", textDecoration: "none" },
  links: { display: "flex", gap: "20px", alignItems: "center" },
  link: { color: "white", textDecoration: "none" },
  button: {
    background: "#2E74B5", color: "white", border: "none",
    padding: "8px 14px", borderRadius: "6px", cursor: "pointer",
  },
};
