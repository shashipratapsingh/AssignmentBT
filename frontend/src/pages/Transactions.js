import React, { useEffect, useState } from "react";
import { apiFetch } from "../api";
import NavBar from "../components/NavBar";

export default function Transactions() {
  const [accounts, setAccounts] = useState([]);
  const [accountId, setAccountId] = useState("");
  const [rows, setRows] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    apiFetch("/api/accounts")
      .then((list) => {
        setAccounts(list);
        if (list.length) setAccountId(String(list[0].id));
      })
      .catch((e) => setError(e.message || "Failed to load accounts"));
  }, []);

  useEffect(() => {
    if (!accountId) return;
    setError("");
    apiFetch(`/api/transactions/account/${accountId}`)
      .then(setRows)
      .catch((e) => setError(e.message || "Failed to load transactions"));
  }, [accountId]);

  return (
    <>
      <NavBar />
      <div className="container">
        <div className="d-flex align-items-center justify-content-between mb-3">
          <h3 className="m-0">Transactions</h3>
          <div style={{ width: 280 }}>
            <select className="form-select" value={accountId} onChange={(e) => setAccountId(e.target.value)}>
              {accounts.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.accountNumber} ({a.accountType})
                </option>
              ))}
            </select>
          </div>
        </div>

        {error ? <div className="alert alert-danger">{error}</div> : null}

        <div className="card">
          <div className="table-responsive">
            <table className="table mb-0">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Type</th>
                  <th>From</th>
                  <th>To</th>
                  <th className="text-end">Amount</th>
                  <th>Time</th>
                </tr>
              </thead>
              <tbody>
                {rows.map((t) => (
                  <tr key={t.id}>
                    <td>{t.id}</td>
                    <td>{t.transactionType}</td>
                    <td>{t.fromAccountId}</td>
                    <td>{t.toAccountId}</td>
                    <td className="text-end">₹ {Number(t.amount).toFixed(2)}</td>
                    <td>{new Date(t.createdAt).toLocaleString()}</td>
                  </tr>
                ))}
                {!rows.length ? (
                  <tr><td colSpan="6" className="text-center text-muted">No transactions</td></tr>
                ) : null}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
}
