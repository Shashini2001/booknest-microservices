import React from "react";
import { Link } from "react-router-dom";

export default function BookCard({ book }) {
  return (
    <Link to={`/books/${book.id}`} style={styles.card}>
      <img
        src={book.coverUrl || "https://via.placeholder.com/150x220?text=Book"}
        alt={book.title}
        style={styles.cover}
      />
      <h4 style={styles.title}>{book.title}</h4>
      <p style={styles.author}>{book.author}</p>
      <p style={styles.price}>Rs. {book.price}</p>
    </Link>
  );
}

const styles = {
  card: {
    display: "block", width: "160px", textDecoration: "none", color: "#222",
    border: "1px solid #eee", borderRadius: "10px", padding: "10px",
  },
  cover: { width: "100%", height: "200px", objectFit: "cover", borderRadius: "6px" },
  title: { fontSize: "14px", margin: "8px 0 2px", color: "#1F3864" },
  author: { fontSize: "12px", color: "#666", margin: 0 },
  price: { fontSize: "13px", fontWeight: "bold", marginTop: "4px" },
};
