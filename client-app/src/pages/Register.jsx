import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { registerUser } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await registerUser(fullName, email, password);
      navigate("/");
    } catch (err) {
      setError("Could not create your account. That email may already be registered.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrap">
      <div className="auth-card">
        <div className="auth-logo">B</div>
        <h2 className="auth-title">Create your BookNest account</h2>
        <form onSubmit={handleSubmit}>
          <div className="field-group">
            <label className="field-label">Full Name</label>
            <input placeholder="Full Name" value={fullName}
              onChange={(e) => setFullName(e.target.value)} required />
          </div>
          <div className="field-group">
            <label className="field-label">Email</label>
            <input type="email" placeholder="email@example.com" value={email}
              onChange={(e) => setEmail(e.target.value)} required />
          </div>
          <div className="field-group">
            <label className="field-label">Password</label>
            <input type="password" placeholder="Password" value={password}
              onChange={(e) => setPassword(e.target.value)} required minLength={6} />
          </div>
          {error && <p className="error-text">{error}</p>}
          <button className="btn btn-primary btn-block" type="submit" disabled={loading}>
            {loading ? "Creating account..." : "Create Account"}
          </button>
        </form>
        <p className="auth-footer">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}
