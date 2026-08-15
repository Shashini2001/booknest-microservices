import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Heart, BookOpen, ShoppingCart } from "lucide-react";
import { getBookById } from "../api/bookApi";
import { addReadingEntry } from "../api/readingApi";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";
import LoadingState from "../components/LoadingState";

export default function BookDetail() {
  const { id } = useParams();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const { addToCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    getBookById(id).then((res) => setBook(res.data)).finally(() => setLoading(false));
  }, [id]);

  const handleAddToCart = () => {
    addToCart(book);
    setMessage("Added to cart.");
  };

  const handleMarkAsReading = async () => {
    if (!user) { navigate("/login"); return; }
    try {
      await addReadingEntry({
        userId: user.id,
        bookId: book.id,
        bookTitle: book.title,
        coverUrl: book.coverUrl,
        status: "READING",
        totalPages: 300,
        pagesRead: 0,
      });
      setMessage("Added to your Currently Reading list.");
    } catch {
      setMessage("Could not update your reading list.");
    }
  };

  const handleAddToFavorites = async () => {
    if (!user) { navigate("/login"); return; }
    try {
      await addReadingEntry({
        userId: user.id,
        bookId: book.id,
        bookTitle: book.title,
        coverUrl: book.coverUrl,
        status: "WISHLIST",
        isFavorite: true,
      });
      setMessage("Added to Favorites.");
    } catch {
      setMessage("Could not add to favorites.");
    }
  };

  if (loading) return <LoadingState text="Loading book..." />;
  if (!book) return <p>Book not found.</p>;

  return (
    <div className="grid-2" style={{ maxWidth: 760 }}>
      <img
        className="book-cover"
        style={{ borderRadius: 14, boxShadow: "var(--shadow-card)" }}
        src={book.coverUrl || `https://placehold.co/300x450/ECE5D6/6B6255?text=${encodeURIComponent(book.title)}`}
        alt={book.title}
      />
      <div>
        <h1 style={{ fontSize: 24, marginBottom: 4 }}>{book.title}</h1>
        <p style={{ color: "var(--ink-soft)", marginBottom: 4 }}>{book.author}</p>
        <p style={{ color: "var(--primary)", fontWeight: 700, fontSize: 18, marginBottom: 16 }}>Rs. {book.price}</p>

        <h4 style={{ fontSize: 13, color: "var(--ink-soft)", marginBottom: 6 }}>Description</h4>
        <p style={{ fontSize: 14, lineHeight: 1.6, marginBottom: 24 }}>
          {book.description || "No description available for this edition yet."}
        </p>

        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          <button className="btn btn-primary" onClick={handleAddToCart}>
            <ShoppingCart size={16} /> Add to Cart
          </button>
          <button className="btn btn-outline" onClick={handleMarkAsReading}>
            <BookOpen size={16} /> Mark as Reading
          </button>
          <button className="btn btn-ghost" onClick={handleAddToFavorites}>
            <Heart size={16} /> Add to Favorites
          </button>
        </div>
        {message && <p style={{ marginTop: 12, fontSize: 13, color: "var(--primary)" }}>{message}</p>}
      </div>
    </div>
  );
}
