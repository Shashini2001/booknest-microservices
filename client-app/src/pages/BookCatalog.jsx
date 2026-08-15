import React, { useEffect, useState } from "react";
import { getAllBooks, getBooksByCategory, searchBooks } from "../api/bookApi";
import BookCard from "../components/BookCard";
import LoadingState from "../components/LoadingState";
import EmptyState from "../components/EmptyState";
import { useSearch } from "../context/SearchContext";

const CATEGORIES = ["All", "Fiction", "Fantasy", "Classics", "Self-Help", "Romance"];

export default function BookCatalog() {
  const [books, setBooks] = useState([]);
  const [category, setCategory] = useState("All");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { query } = useSearch();

  const load = (cat) => {
    setLoading(true);
    setError("");
    const request = cat === "All" ? getAllBooks() : getBooksByCategory(cat);
    request
      .then((res) => setBooks(res.data))
      .catch(() => setError("Could not reach the Book Catalog Service. Is it running on the gateway?"))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    if (query) {
      setLoading(true);
      searchBooks(query)
        .then((res) => setBooks(res.data))
        .catch(() => setError("Search failed."))
        .finally(() => setLoading(false));
    } else {
      load(category);
    }
    /* eslint-disable-next-line */
  }, [category, query]);

  return (
    <div>
      <h1 style={{ fontSize: 26, marginBottom: 18 }}>Home Page</h1>

      <div style={{ display: "flex", gap: 10, marginBottom: 24, flexWrap: "wrap" }}>
        {CATEGORIES.map((c) => (
          <button
            key={c}
            className={`chip${category === c ? " active" : ""}`}
            onClick={() => setCategory(c)}
          >
            {c}
          </button>
        ))}
      </div>

      {loading && <LoadingState text="Loading books..." />}
      {error && <p className="error-text">{error}</p>}
      {!loading && !error && books.length === 0 && (
        <EmptyState title="No books here yet" subtitle="Try a different category or check back soon." />
      )}

      <div className="book-grid">
        {books.map((b) => <BookCard key={b.id} book={b} />)}
      </div>
    </div>
  );
}
