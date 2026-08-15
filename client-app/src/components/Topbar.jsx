import React, { useState, useEffect } from "react";
import { Search } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useSearch } from "../context/SearchContext";

export default function Topbar({ placeholder = "Search books, authors, editions..." }) {
  const { query, setQuery } = useSearch();
  const [local, setLocal] = useState(query);
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => { setLocal(query); }, [query]);

  const initials = user && user.fullName
    ? user.fullName.split(" ").map((p) => p[0]).slice(0, 2).join("").toUpperCase()
    : "?";

  const handleKeyDown = (e) => {
    if (e.key === "Enter") setQuery(local);
  };

  return (
    <div className="topbar">
      <div className="search-box">
        <Search size={16} />
        <input
          placeholder={placeholder}
          value={local}
          onChange={(e) => setLocal(e.target.value)}
          onKeyDown={handleKeyDown}
        />
      </div>
      <div className="topbar-user" onClick={() => navigate("/profile")} style={{ cursor: "pointer" }}>
        <span>{user ? user.fullName : "Guest"}</span>
        <div className="avatar">{initials}</div>
      </div>
    </div>
  );
}
