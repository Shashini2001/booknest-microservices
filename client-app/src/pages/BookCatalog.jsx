import React, { useEffect, useState } from "react";
import { getAllBooks, getBooksByCategory } from "../api/bookApi";
import BookCard from "../components/BookCard";

const CATEGORIES = ["All", "Fiction", "Fantasy", "Self-Help", "Romance", "Non-Fiction"];

export default function BookCatalog() {
  const [books, setBooks] = useState([]);
  const [category, setCategory] = useState("All");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    const request = category === "All" ? getAllBooks() : getBooksByCategory(category);
    request
      .then((res) => setBooks(res.data))
      .catch(() => setError("Could not load books. Is the backend running?"))
      .finally(() => setLoading(false));
  }, [category]);

  return (
    <div style={{ padding: 24 }}>
      <h2>Browse Books</h2>

      <div style={{ display: "flex", gap: 8, marginBottom: 20 }}>
        {CATEGORIES.map((c) => (
          <button
            key={c}
            onClick={() => setCategory(c)}
            style={{
              padding: "6px 14px", borderRadius: 20, border: "1px solid #1F3864",
              background: category === c ? "#1F3864" : "white",
              color: category === c ? "white" : "#1F3864", cursor: "pointer",
            }}
          >
            {c}
          </button>
        ))}
      </div>

      {loading && <p>Loading books...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      <div style={{ display: "flex", flexWrap: "wrap", gap: 16 }}>
        {books.map((book) => (
          <BookCard key={book.id} book={book} />
        ))}
      </div>
    </div>
  );
}
