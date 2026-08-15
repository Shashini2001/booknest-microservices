import React from "react";
import { Link } from "react-router-dom";
import { Star } from "lucide-react";

export default function BookCard({ book }) {
  return (
    <Link to={`/books/${book.id}`} className="book-card">
      <img
        className="book-cover"
        src={book.coverUrl || `https://placehold.co/300x450/ECE5D6/6B6255?text=${encodeURIComponent(book.title || "Book")}`}
        alt={book.title}
      />
      <div className="book-info">
        <div className="book-title">{book.title}</div>
        <div className="book-author">{book.author}</div>
        <div className="book-meta">
          <span className="book-price">Rs. {book.price}</span>
          <span className="book-rating"><Star size={13} fill="currentColor" /> {book.rating || "New"}</span>
        </div>
      </div>
    </Link>
  );
}
