import React, { createContext, useContext, useState, useEffect } from "react";
import * as authApi from "../api/authApi";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const saved = localStorage.getItem("user");
    if (saved) setUser(JSON.parse(saved));
    setLoading(false);
  }, []);

  const loginUser = async (email, password) => {
    const res = await authApi.login(email, password);
    localStorage.setItem("token", res.data.token);
    const u = { id: res.data.userId, fullName: res.data.fullName, email: res.data.email, role: res.data.role };
    localStorage.setItem("user", JSON.stringify(u));
    setUser(u);
    return u;
  };

  const registerUser = async (fullName, email, password) => {
    const res = await authApi.register(fullName, email, password);
    localStorage.setItem("token", res.data.token);
    const u = { id: res.data.userId, fullName: res.data.fullName, email: res.data.email, role: res.data.role };
    localStorage.setItem("user", JSON.stringify(u));
    setUser(u);
    return u;
  };

  const logoutUser = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, loginUser, registerUser, logoutUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
