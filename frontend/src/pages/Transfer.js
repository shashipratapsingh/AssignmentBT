import React, { useEffect, useState } from "react";
import { apiFetch } from "../api";
import NavBar from "../components/NavBar";

export default function Transfer() {
  const [accounts, setAccounts] = useState([]);
  const [fromAccountId, setFromAccountId] = useState("");
  const [toAccountNumber, setToAccountNumber] = useState("");
  const [amount, setAmount] = useState("");
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    apiFetch("/api/accounts")
      .then((list) => {
        setAccounts(list);
        if (list.length) setFromAccountId(String(list[0].id));
      })
      .catch((e) => setError(e.message || "Failed to load accounts"));
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    setMsg("");
    setError("");

    try {
      const payload = {
        fromAccountId: Number(fromAccountId),
        toAccountNumber: toAccountNumber.trim(),
        amount: Number(amount)
      };

      const res = await apiFetch("/api/transactions/transfer", {
        method: "POST",
        body: JSON.stringify(payload)
      });

      setMsg(res.message || "Transfer successful");
      setToAccountNumber("");
      setAmount("");
    } catch (err) {
      setError(err.message || "Transfer failed");
    }
  };

  return (
    <>
      <NavBar />
      <div className="container" style={{ maxWidth: 640 }}>
        <h3 className="mb-3">Transfer Money</h3>

        {msg ? <div className="alert alert-success">{msg}</div> : null}
        {error ? <div className="alert alert-danger">{error}</div> : null}

        <form onSubmit={submit} className="card">
          <div className="card-body">
            <div className="mb-3">
              <label className="form-label">From Account</label>
              <select className="form-select" value={fromAccountId} onChange={(e) => setFromAccountId(e.target.value)}>
                {accounts.map((a) => (
                  <option key={a.id} value={a.id}>
                    {a.accountNumber} ({a.accountType}) - ₹ {Number(a.balance).toFixed(2)}
                  </option>
                ))}
              </select>
            </div>

            <div className="mb-3">
              <label className="form-label">To Account Number</label>
              <input className="form-control" value={toAccountNumber} onChange={(e) => setToAccountNumber(e.target.value)} placeholder="10-digit account number" />
            </div>

            <div className="mb-3">
              <label className="form-label">Amount</label>
              <input className="form-control" value={amount} onChange={(e) => setAmount(e.target.value)} placeholder="e.g. 100.00" />
            </div>

            <button className="btn btn-primary" type="submit">Send</button>
          </div>
        </form>
      </div>
    </>
  );
}
