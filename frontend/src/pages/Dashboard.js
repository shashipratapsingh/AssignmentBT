import React, { useEffect, useMemo, useState } from "react";
import { apiFetch } from "../api";
import NavBar from "../components/NavBar";
import { Link } from "react-router-dom";

export default function Dashboard() {
  const [accounts, setAccounts] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    apiFetch("/api/accounts")
      .then(setAccounts)
      .catch((e) => setError(e.message || "Failed to load accounts"));
  }, []);

  const total = useMemo(() => accounts.reduce((sum, a) => sum + Number(a.balance || 0), 0), [accounts]);

  return (
    <>
      <NavBar />
      <div className="container">
        <div className="d-flex align-items-center justify-content-between mb-3">
          <h3 className="m-0">Dashboard</h3>
          <Link className="btn btn-success" to="/transfer">Quick Transfer</Link>
        </div>

        {error ? <div className="alert alert-danger">{error}</div> : null}

        <div className="card mb-3">
          <div className="card-body">
            <div className="fw-bold">Total Balance</div>
            <div style={{ fontSize: 28 }}>₹ {total.toFixed(2)}</div>
          </div>
        </div>

        <div className="card">
          <div className="card-header fw-bold">My Accounts</div>
          <div className="table-responsive">
            <table className="table mb-0">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Account #</th>
                  <th>Type</th>
                  <th className="text-end">Balance</th>
                </tr>
              </thead>
              <tbody>
                {accounts.map((a) => (
                  <tr key={a.id}>
                    <td>{a.id}</td>
                    <td>{a.accountNumber}</td>
                    <td>{a.accountType}</td>
                    <td className="text-end">₹ {Number(a.balance).toFixed(2)}</td>
                  </tr>
                ))}
                {!accounts.length ? (
                  <tr><td colSpan="4" className="text-center text-muted">No accounts found</td></tr>
                ) : null}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
}
