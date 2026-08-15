import React from "react";
import { NavLink } from "react-router-dom";
import { Home, LayoutDashboard, ShoppingCart, Package, User, ShieldCheck } from "lucide-react";
import { useAuth } from "../context/AuthContext";

export default function Sidebar() {
  const { user } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">B</div>

      <NavLink to="/" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`} title="Browse">
        <Home size={20} />
      </NavLink>
      <NavLink to="/dashboard" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`} title="Reading Dashboard">
        <LayoutDashboard size={20} />
      </NavLink>
      <NavLink to="/cart" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`} title="Cart">
        <ShoppingCart size={20} />
      </NavLink>
      <NavLink to="/orders" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`} title="Orders">
        <Package size={20} />
      </NavLink>

      <div className="sidebar-bottom">
        {user && user.role === "ADMIN" && (
          <NavLink to="/admin" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`} title="Admin">
            <ShieldCheck size={20} />
          </NavLink>
        )}
        <NavLink to="/profile" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`} title="Profile">
          <User size={20} />
        </NavLink>
      </div>
    </aside>
  );
}
