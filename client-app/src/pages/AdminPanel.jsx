import React, { useEffect, useState } from "react";
import { getAllBooks, deleteBook } from "../api/bookApi";
import LoadingState from "../components/LoadingState";

export default function AdminPanel() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    setLoading(true);
    getAllBooks().then((res) => setBooks(res.data)).finally(() => setLoading(false));
  };

  useEffect(load, []);

  const handleDelete = async (id) => {
    await deleteBook(id);
    load();
  };

  if (loading) return <LoadingState text="Loading catalog..." />;

  return (
    <div>
      <h1 style={{ fontSize: 22, marginBottom: 18 }}>Admin Dashboard</h1>
      <div className="card">
        <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 13 }}>
          <thead>
            <tr style={{ textAlign: "left", color: "var(--ink-soft)" }}>
              <th style={{ padding: "8px 10px", borderBottom: "1px solid var(--line)" }}>Title</th>
              <th style={{ padding: "8px 10px", borderBottom: "1px solid var(--line)" }}>Category</th>
              <th style={{ padding: "8px 10px", borderBottom: "1px solid var(--line)" }}>Price</th>
              <th style={{ padding: "8px 10px", borderBottom: "1px solid var(--line)" }}>Stock</th>
              <th style={{ padding: "8px 10px", borderBottom: "1px solid var(--line)" }}></th>
            </tr>
          </thead>
          <tbody>
            {books.map((b) => (
              <tr key={b.id}>
                <td style={{ padding: "10px", borderBottom: "1px solid var(--line)" }}>{b.title}</td>
                <td style={{ padding: "10px", borderBottom: "1px solid var(--line)" }}>{b.category}</td>
                <td style={{ padding: "10px", borderBottom: "1px solid var(--line)" }}>Rs. {b.price}</td>
                <td style={{ padding: "10px", borderBottom: "1px solid var(--line)" }}>{b.stock}</td>
                <td style={{ padding: "10px", borderBottom: "1px solid var(--line)" }}>
                  <button className="btn btn-ghost btn-sm" onClick={() => handleDelete(b.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
