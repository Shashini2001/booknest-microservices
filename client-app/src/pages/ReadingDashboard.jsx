import React, { useEffect, useState } from "react";
import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer, Tooltip, CartesianGrid } from "recharts";
import { getForUser, getStats } from "../api/readingApi";
import { useAuth } from "../context/AuthContext";
import LoadingState from "../components/LoadingState";
import EmptyState from "../components/EmptyState";

export default function ReadingDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ totalBooksRead: 0, currentlyReading: 0, byMonth: {} });
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) return;
    Promise.all([getStats(user.id), getForUser(user.id)])
      .then(([statsRes, entriesRes]) => {
        setStats(statsRes.data);
        setEntries(entriesRes.data);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [user]);

  if (loading) return <LoadingState text="Loading your reading dashboard..." />;

  const chartData = Object.entries(stats.byMonth || {}).map(([month, count]) => ({ month, count }));
  const reading = entries.filter((e) => e.status === "READING");
  const completed = entries.filter((e) => e.status === "COMPLETED");

  return (
    <div>
      <h1 style={{ fontSize: 24, marginBottom: 18 }}>Reading Dashboard</h1>

      <div className="stat-row">
        <div className="stat-card">
          <div className="stat-number">{stats.totalBooksRead}</div>
          <div className="stat-label">Books read</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{chartData.length}</div>
          <div className="stat-label">Active months</div>
        </div>
      </div>

      <div className="dash-grid">
        <div className="card">
          <h3 className="section-title">Books read per month</h3>
          {chartData.length === 0 ? (
            <EmptyState title="No completed books yet" subtitle="Finish a book to see your progress chart." />
          ) : (
            <ResponsiveContainer width="100%" height={220}>
              <BarChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--line)" vertical={false} />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: "var(--ink-soft)" }} axisLine={false} tickLine={false} />
                <YAxis allowDecimals={false} tick={{ fontSize: 12, fill: "var(--ink-soft)" }} axisLine={false} tickLine={false} />
                <Tooltip cursor={{ fill: "var(--paper-dim)" }} />
                <Bar dataKey="count" fill="var(--primary)" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        <div>
          <div className="card" style={{ marginBottom: 20 }}>
            <h3 className="section-title">Currently Reading</h3>
            {reading.length === 0 && <p style={{ fontSize: 13, color: "var(--ink-soft)" }}>Nothing in progress.</p>}
            {reading.map((r) => (
              <div key={r.id} className="reading-row">
                <img className="reading-thumb" src={r.coverUrl || "https://placehold.co/80x112/ECE5D6/6B6255?text=Book"} alt="" />
                <div className="reading-row-info">
                  <div className="reading-row-title">{r.bookTitle}</div>
                  <div className="reading-row-sub">Page {r.pagesRead} of {r.totalPages || "?"}</div>
                  <div className="progress-track">
                    <div className="progress-fill" style={{ width: `${r.totalPages ? Math.min(100, (r.pagesRead / r.totalPages) * 100) : 0}%` }} />
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="card">
            <h3 className="section-title">Completed</h3>
            {completed.length === 0 && <p style={{ fontSize: 13, color: "var(--ink-soft)" }}>No completed books yet.</p>}
            {completed.map((r) => (
              <div key={r.id} className="reading-row">
                <img className="reading-thumb" src={r.coverUrl || "https://placehold.co/80x112/ECE5D6/6B6255?text=Book"} alt="" />
                <div className="reading-row-info">
                  <div className="reading-row-title">{r.bookTitle}</div>
                  <div className="reading-row-sub">Finished</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
